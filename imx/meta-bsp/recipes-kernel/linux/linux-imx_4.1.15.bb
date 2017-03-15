# Copyright (C) 2013-2016 Freescale Semiconductor
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "Linux Kernel provided and supported by Freescale"
DESCRIPTION = "Linux Kernel provided and supported by Freescale with focus on \
i.MX Family Reference Boards. It includes support for many IPs such as GPU, VPU and IPU."

require recipes-kernel/linux/linux-imx.inc
require recipes-kernel/linux/linux-dtb.inc

DEPENDS += "lzop-native bc-native"

SRCBRANCH = "imx_4.1.15_2.0.0_ga"
LOCALVERSION = "-2.0.3"
SRCREV = "b63f3f52cb393e3287352cf63f0caef31a33ab63"
KERNEL_SRC ?= "git://git.freescale.com/imx/linux-imx.git;protocol=git"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"

MX6ULL_900MHZ_PATCH = "file://0008-MLK-14409-01-ARM-imx-Add-speed-grading-fuse-check-fo.patch \
                       file://0009-MLK-14409-02-ARM-dts-imx-Add-900MHz-setpoint-on-i.mx.patch \
                       file://0010-MLK-14409-03-ARM-dts-imx-Correct-the-setpoint-on-imx.patch \
"

SRC_URI += "file://0001-ARM-imx-imx6ul-add-PHY-KSZ8081-new-silicon-revision-.patch \
            file://0001-MLK-13418-ASoC-wm8960-workaround-no-sound-issue-in-m.patch \
            file://0002-MLK-13422-ASoC-wm8960-fix-the-pitch-shift-issue-afte.patch \
            file://0001-MLK-13748-ARM-dts-imx6ull-9x9-evk-ldo-add-ldo-enable.patch \
            file://0002-MLK-13766-ARM-dts-imx6ull-9x9-evk-correct-the-xnur-p.patch \
            file://0003-MLK-13774-ARM-imx-fix-lpddr2-busfreq-support-on-i.mx.patch \
            file://0004-MLK-13724-ARM-dts-fix-audio-error-log-in-kernel-boot.patch \
            file://0005-MLK-13601-01-ARM-dts-imx-update-the-setpoint-for-imx.patch \
            file://0006-MLK-13601-02-ARM-imx-Add-fuse-check-support-for-imx6.patch \
            file://0007-MLK-13616-ARM-imx-Add-low-power-run-voltage-change-s.patch \
            ${MX6ULL_900MHZ_PATCH} \
"

DEFAULT_PREFERENCE = "1"

DO_CONFIG_V7_COPY = "no"
DO_CONFIG_V7_COPY_mx6 = "yes"
DO_CONFIG_V7_COPY_mx6ul = "yes"
DO_CONFIG_V7_COPY_mx7 = "yes"

addtask copy_defconfig after do_patch before do_preconfigure #do_configure
do_copy_defconfig () {
    if [ ${DO_CONFIG_V7_COPY} = "yes" ]; then
        # copy latest imx_v7_defconfig to use for mx6, mx6ul and mx7
        mkdir -p ${B}
        cp ${S}/arch/arm/configs/imx_v7_defconfig ${B}/.config
        cp ${S}/arch/arm/configs/imx_v7_defconfig ${B}/../defconfig
    fi
}

COMPATIBLE_MACHINE = "(mx6|mx6ul|mx7)"
