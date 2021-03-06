# Copyright 2017 NXP

DESCRIPTION = "Generate Boot Loader for i.MX8 device"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
SECTION = "BSP"

require imx-mkimage_git.inc

inherit deploy

BOOT_TOOLS = "imx-boot-tools"
BOOT_NAME = "imx-boot"
PROVIDES = "${BOOT_NAME}"

IMX_FIRMWARE       = "imx-sc-firmware"
IMX_FIRMWARE_mx8mq = "firmware-imx"
DEPENDS += " \
    u-boot \
    ${IMX_FIRMWARE} \
    imx-atf \
"

# For i.MX 8, this package aggregates the imx-m4-demos
# output. Note that this aggregation replaces the aggregation
# that would otherwise be done in the image build as controlled
# by IMAGE_M4LOADER and IMAGE_M4 in image_types_fsl.bbclass
IMX_M4_DEMOS        = ""
IMX_M4_DEMOS_mx8qm  = "imx-m4-demos:do_deploy"
IMX_M4_DEMOS_mx8qxp = "imx-m4-demos:do_deploy"

# This package aggregates output deployed by other packages,
# so set the appropriate dependencies
do_compile[depends] += " \
    virtual/bootloader:do_deploy \
    ${IMX_FIRMWARE}:do_deploy \
    imx-atf:do_deploy \
    ${IMX_M4_DEMOS} \
"

SC_FIRMWARE_NAME ?= "scfw_tcm.bin"

ATF_MACHINE_NAME ?= "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qm = "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qxp = "bl31-imx8qxp.bin"
ATF_MACHINE_NAME_mx8mq = "bl31-imx8mq.bin"

DCD_NAME ?= "imx8qm_dcd.cfg.tmp"
DCD_NAME_mx8qm = "imx8qm_dcd.cfg.tmp"
DCD_NAME_apalis-imx8 = "imx8qm_apalis-imx8_dcd.cfg.tmp"
DCD_NAME_mx8qxp = "imx8qx_dcd.cfg.tmp"

DCD_BOARD ?= ""
DCD_BOARD_apalis-imx8 = "apalis-imx8"

UBOOT_NAME = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
BOOT_CONFIG_MACHINE = "${BOOT_NAME}-${MACHINE}-${UBOOT_CONFIG}.bin"

TOOLS_NAME ?= "mkimage_imx8"

SOC_TARGET ?= "iMX8QM"
SOC_TARGET_mx8qm  = "iMX8QM"
SOC_TARGET_mx8qxp = "iMX8QX"
SOC_TARGET_mx8mq  = "iMX8M"

IMXBOOT_TARGETS ?= "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', 'flash_flexspi', \
                       bb.utils.contains('UBOOT_CONFIG', 'nand', 'flash_nand', \
                                                                 'flash flash_dcd flash_multi_cores', d), d)}"
IMXBOOT_TARGETS_mx8mq ?= "flash_spl_uboot flash_hdmi_spl_uboot"
IMXBOOT_TARGETS_imx8qxpddr3arm2 ?= "flash_ddr3_dcd"

S = "${WORKDIR}/git"

do_compile () {
    if [ "${SOC_TARGET}" = "iMX8M" ]; then
        echo 8MQ boot binary build
        cp ${DEPLOY_DIR_IMAGE}/lpddr4_pmu_train_*.bin            ${S}/${SOC_TARGET}/
        cp ${DEPLOY_DIR_IMAGE}/signed_hdmi_imx8m.bin             ${S}/${SOC_TARGET}/
        cp ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} ${S}/${SOC_TARGET}/u-boot-spl.bin
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/fsl-imx8mq-evk.dtb  ${S}/${SOC_TARGET}/
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/u-boot-nodtb.bin    ${S}/${SOC_TARGET}/
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/mkimage_uboot       ${S}/${SOC_TARGET}/

        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${S}/${SOC_TARGET}/bl31.bin
        cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME}                     ${S}/${SOC_TARGET}/u-boot.bin

    elif [ "${SOC_TARGET}" = "iMX8QM" ]; then
        echo 8QM boot binary build
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_FIRMWARE_NAME} ${S}/${SOC_TARGET}/scfw_tcm.bin
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${S}/${SOC_TARGET}/bl31.bin
        cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME}                     ${S}/${SOC_TARGET}/u-boot.bin

        cp ${DEPLOY_DIR_IMAGE}/imx8qm_m4_0_TCM_rpmsg_lite_pingpong_rtos_linux_remote.bin ${S}/${SOC_TARGET}/m40_tcm.bin
        cp ${DEPLOY_DIR_IMAGE}/imx8qm_m4_1_TCM_rpmsg_lite_pingpong_rtos_linux_remote.bin ${S}/${SOC_TARGET}/m41_tcm.bin

    else
        echo 8QX boot binary build
        cp ${DEPLOY_DIR_IMAGE}/imx8qx_m4_TCM_rpmsg_lite_pingpong_rtos_linux_remote.bin ${S}/${SOC_TARGET}/m40_tcm.bin
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_FIRMWARE_NAME} ${S}/${SOC_TARGET}/scfw_tcm.bin
        cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME} ${S}/${SOC_TARGET}/bl31.bin
        cp ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME}                     ${S}/${SOC_TARGET}/u-boot.bin
    fi

    # mkimage for i.MX8
    for target in ${IMXBOOT_TARGETS}; do
        echo "building ${SOC_TARGET} - ${target} (${DCD_BOARD})"
        make DCD_BOARD=${DCD_BOARD} SOC=${SOC_TARGET} ${target}
        if [ -e "${S}/${SOC_TARGET}/flash.bin" ]; then
            cp ${S}/${SOC_TARGET}/flash.bin ${S}/${BOOT_CONFIG_MACHINE}-${target}
        fi
    done
}

SYSROOT_DIRS += "/boot"

do_install () {
    install -d ${D}/boot
    for target in ${IMXBOOT_TARGETS}; do
        install -m 0644 ${S}/${BOOT_CONFIG_MACHINE}-${target} ${D}/boot/
    done
}

DEPLOYDIR_IMXBOOT = "${BOOT_TOOLS}"
do_deploy () {
    install -d ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}

    # copy the tool mkimage to deploy path and sc fw, dcd and uboot
    install -m 0644 ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME} ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}
    if [ "${SOC_TARGET}" = "iMX8M" ]; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}
        install -m 0644 ${DEPLOY_DIR_IMAGE}/lpddr4_pmu_train_*.bin ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}
        install -m 0644 ${DEPLOY_DIR_IMAGE}/signed_hdmi*.bin ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}

        install -m 0755 ${S}/${SOC_TARGET}/${TOOLS_NAME} ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}

        install -m 0755 ${S}/${SOC_TARGET}/mkimage_fit_atf.sh ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}
    else
        install -m 0644 ${S}/${SOC_TARGET}/${DCD_NAME} ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}

        install -m 0755 ${S}/${TOOLS_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    # copy makefile (soc.mak) for reference
    install -m 0644 ${S}/${SOC_TARGET}/soc.mak     ${DEPLOYDIR}/${DEPLOYDIR_IMXBOOT}

    # copy the generated boot image to deploy path
    for target in ${IMXBOOT_TARGETS}; do
        install -m 0644 ${S}/${BOOT_CONFIG_MACHINE}-${target} ${DEPLOYDIR}
    done
    cd ${DEPLOYDIR}
    ln -sf ${BOOT_CONFIG_MACHINE}-${target} ${BOOT_CONFIG_MACHINE}
    cd -
}

addtask deploy before do_build after do_compile

FILES_${PN} = "/boot"

COMPATIBLE_MACHINE = "(mx8qm|mx8qxp|mx8mq)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
