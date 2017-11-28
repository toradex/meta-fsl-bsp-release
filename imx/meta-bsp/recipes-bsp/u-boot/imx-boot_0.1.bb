# Copyright 2017 NXP

DESCRIPTION = "Generate Boot Loader for i.MX8 device"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0;md5=801f80980d171dd6425610833a22dbe6"
SECTION = "BSP"

inherit deploy

BOOT_TOOLS = "imx-boot-tools"
BOOT_NAME = "imx-boot"
PROVIDES = "${BOOT_NAME}"

DEPENDS += "imx-sc-firmware u-boot imx-mkimage imx-atf"
DEPENDS_remove_mx8mq = "imx-sc-firmware"
DEPENDS_append_mx8mq = " firmware-imx"
DEPENDS_append_mx8qm = " imx-m4-demos"

SC_MACHINE_NAME ?= "mx8qm-scfw-tcm.bin"
SC_MACHINE_NAME_mx8qm = "mx8qm-scfw-tcm.bin"
SC_MACHINE_NAME_mx8qxp = "mx8qx-scfw-tcm.bin"

ATF_MACHINE_NAME ?= "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qm = "bl31-imx8qm.bin"
ATF_MACHINE_NAME_mx8qxp = "bl31-imx8qxp.bin"
ATF_MACHINE_NAME_mx8mq = "bl31-imx8mq.bin"

DCD_NAME ?= "imx8qm_dcd.cfg.tmp"
DCD_NAME_mx8qm = "imx8qm_dcd.cfg.tmp"
DCD_NAME_mx8qxp = "imx8qx_dcd.cfg.tmp"

UBOOT_NAME = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
UBOOT_NAME_ATF = "u-boot-atf-${MACHINE}.bin-${UBOOT_CONFIG}"
BOOT_CONFIG_MACHINE = "${BOOT_NAME}-${MACHINE}-${UBOOT_CONFIG}.bin"
BOOT_CONFIG_MACHINE_NODCD = "${BOOT_CONFIG_MACHINE}-flash"
BOOT_CONFIG_MACHINE_DCD = "${BOOT_CONFIG_MACHINE}-flash_dcd"
BOOT_CONFIG_MACHINE_NOHDMI = "${BOOT_CONFIG_MACHINE}-no_hdmi"

MX8_BOOT_CORE = "${@bb.utils.contains('UBOOT_CONFIG', 'basic2ca72', 'a72', 'a53', d)}"
MX8_BOOT_CORE_mx8qxp = "a35"

MX8_BOOT_OPTIONS = "${@bb.utils.contains('UBOOT_CONFIG', 'fspi', '-dev flexspi', \
                       bb.utils.contains('UBOOT_CONFIG', 'nand', '-dev nand', \
                                                                 '', d), d)}"

TOOLS_NAME ?= "mkimage_imx8"
TOOLS_NAME_mx8mq = "mkimage_imx8m"
TOOLS_NAME_mx8qm = "mkimage_imx8"
TOOLS_NAME_mx8qxp = "mkimage_imx8"

SOC_TARGET ?= "QM"
SOC_TARGET_mx8qxp = "QX"

IS_MX8MQ ?= "0"
IS_MX8MQ_mx8mq = "1"

IS_MX8QM ?= "0"
IS_MX8QM_mx8qm = "1"

# Inter-Task dependeency for do_compile task
COMPILE_DEP_TASKS ?= ""
COMPILE_DEP_TASKS_mx8qm = "imx-sc-firmware:do_deploy imx-m4-demos:do_deploy"
COMPILE_DEP_TASKS_mx8qxp = "imx-sc-firmware:do_deploy imx-m4-demos:do_deploy"
COMPILE_DEP_TASKS_mx8mq = "firmware-imx:do_deploy"

do_compile[depends] = "imx-mkimage:do_deploy \
                       imx-atf:do_deploy \
                       virtual/bootloader:do_deploy \
                       ${COMPILE_DEP_TASKS}"

do_compile () {
    # Combine ATF with u-boot.bin
    cp ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${ATF_MACHINE_NAME}  ${UBOOT_NAME_ATF}
    if [ "${IS_MX8MQ}" != "1" ]; then
        ${TOOLS_NAME} -commit > head.hash
        cat ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME} head.hash > u-boot-hash.bin
        dd if=u-boot-hash.bin of=${UBOOT_NAME_ATF}  bs=1K seek=128
    else
        dd if=${DEPLOY_DIR_IMAGE}/${UBOOT_NAME} of=${UBOOT_NAME_ATF}  bs=1K seek=128
    fi

    if [ "${IS_MX8MQ}" = "1" ]; then
        # generate u-boot-spl-ddr.bin
        objcopy -I binary -O binary --pad-to 0x8000 --gap-fill=0x0 \
                   ${DEPLOY_DIR_IMAGE}/lpddr4_pmu_train_imem.bin lpddr4_pmu_train_imem_pad.bin
        cat lpddr4_pmu_train_imem_pad.bin ${DEPLOY_DIR_IMAGE}/lpddr4_pmu_train_dmem.bin > lpddr4_pmu_train_fw.bin
        cat ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} lpddr4_pmu_train_fw.bin > \
                   u-boot-spl-ddr-${MACHINE}.bin-${UBOOT_CONFIG}
        rm -f lpddr4_pmu_train_fw.bin lpddr4_pmu_train_imem_pad.bin

        # mkimage_imx8 for i.MX8MQ
        # flash_spl_uboot.bin
        #./mkimage_imx8 -loader u-boot-spl-ddr.bin 0x7E1000 -second_loader u-boot-atf.bin 0x40001000 0x60000 -out $(OUTIMG)
        ${TOOLS_NAME} \
                 -loader u-boot-spl-ddr-${MACHINE}.bin-${UBOOT_CONFIG} 0x7E1000 \
                 -second_loader ${UBOOT_NAME_ATF} 0x40001000 0x60000 \
                 -out ${BOOT_CONFIG_MACHINE_NOHDMI}

        # flash_hdmi_spl_uboot.bin
        # ./mkimage_imx8 -hdmi hdmi_imx8m.bin -loader u-boot-spl-ddr.bin 0x7E1000 -second_loader u-boot-atf.bin 0x40001000 0x60000 -out $(OUTIMG)
        ${TOOLS_NAME} \
                 -hdmi ${DEPLOY_DIR_IMAGE}/hdmi_imx8m.bin \
                 -loader u-boot-spl-ddr-${MACHINE}.bin-${UBOOT_CONFIG} 0x7E1000 \
                 -second_loader ${UBOOT_NAME_ATF} 0x40001000 0x60000 \
                 -out ${BOOT_CONFIG_MACHINE}

    elif [ "${IS_MX8QM}" = "1" ]; then
        cp ${DEPLOY_DIR_IMAGE}/imx8qm_m4_0_TCM_rpmsg_lite_pingpong_rtos_linux_remote.bin m40_tcm.bin
        cp ${DEPLOY_DIR_IMAGE}/imx8qm_m4_1_TCM_rpmsg_lite_pingpong_rtos_linux_remote.bin m41_tcm.bin

        # mkimage for i.MX8QM
        if [ "${UBOOT_CONFIG}" = "fspi" ]; then
            # flash_flexspi:
            # ./$(MKIMG) -soc QM -c -dev flexspi -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a53 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE}

        else
            # flash:
            # ./$(MKIMG) -soc QX -c -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE_NODCD}

            # flash_dcd:
            # ./$(MKIMG) -soc QM -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a53 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE_DCD}

            # flash_multi_cores:
            # ./$(MKIMG) -soc QM -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -m4 m40_tcm.bin 0 0x34FE0000 -m4 m41_tcm.bin 1 0x38FE0000 -c -ap u-boot-atf.bin a53 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -m4 m40_tcm.bin 0 0x34fe0000 -m4 m41_tcm.bin 1 0x38fe0000 \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE}
        fi

    else
        # mkimage for i.MX8QX
        if [ "${UBOOT_CONFIG}" = "fspi" ]; then
            # flash_flexspi:
            # ./$(MKIMG) -soc QX -dev flexspi -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE}

        elif [ "${UBOOT_CONFIG}" = "nand" ]; then
            # flash_nand:
            # ./$(MKIMG) -soc QX -dev nand -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE}

        else
            # flash:
            # ./$(MKIMG) -soc QX -c -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE_NODCD}

            # flash_dcd:
            # ./$(MKIMG) -soc QX -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE_DCD}

            # flash_multi_cores:
            # ./$(MKIMG) -soc QX -c -dcd $(DCD_CFG) -scfw scfw_tcm.bin -m4 m40_tcm.bin 0 0x34FE0000 -c -ap u-boot-atf.bin a35 0x80000000 -out flash.bin
            ${TOOLS_NAME} -soc ${SOC_TARGET} \
                 ${MX8_BOOT_OPTIONS} \
                 -c -dcd  ${DEPLOY_DIR_IMAGE}/${DCD_NAME} \
                 -scfw ${DEPLOY_DIR_IMAGE}/${BOOT_TOOLS}/${SC_MACHINE_NAME} \
                 -m4 ${DEPLOY_DIR_IMAGE}/imx8qx_m4_hello_world.bin 0 0x34fe0000 \
                 -c -ap ${UBOOT_NAME_ATF} ${MX8_BOOT_CORE} 0x80000000 \
                 -out ${BOOT_CONFIG_MACHINE}
        fi
    fi
}

SYSROOT_DIRS += "/boot"

do_install () {
    install -d ${D}/boot
    install -m 0644 ${BOOT_CONFIG_MACHINE} ${D}/boot/
}

do_deploy () {
    install -d ${DEPLOYDIR}/${BOOT_TOOLS}

    # copy the tool mkimage to deploy path and sc fw, dcd and uboot
    install -m 0644 ${DEPLOY_DIR_IMAGE}/${UBOOT_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}
    install -m 0755 ${STAGING_BINDIR_NATIVE}/${TOOLS_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}
    if [ "${IS_MX8MQ}" = "1" ]; then
        install -m 0644 ${DEPLOY_DIR_IMAGE}/u-boot-spl.bin-${MACHINE}-${UBOOT_CONFIG} ${DEPLOYDIR}/${BOOT_TOOLS}
        install -m 0644 ${DEPLOY_DIR_IMAGE}/lpddr4_pmu_train_*.bin ${DEPLOYDIR}/${BOOT_TOOLS}
        install -m 0644 ${DEPLOY_DIR_IMAGE}/hdmi*.bin ${DEPLOYDIR}/${BOOT_TOOLS}
    else
        install -m 0644 ${DEPLOY_DIR_IMAGE}/${DCD_NAME} ${DEPLOYDIR}/${BOOT_TOOLS}
    fi

    # copy the generated boot image to deploy path
    install -m 0644 ${S}/${BOOT_CONFIG_MACHINE}* ${DEPLOYDIR}/${BOOT_TOOLS}
}

addtask deploy before do_build after do_compile

FILES_${PN} = "/boot"

COMPATIBLE_MACHINE = "(mx8qm|mx8qxp|mx8mq)"
PACKAGE_ARCH = "${MACHINE_ARCH}"
