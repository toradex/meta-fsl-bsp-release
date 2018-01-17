# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP

DESCRIPTION = "i.MX U-Boot suppporting i.MX reference boards."
require recipes-bsp/u-boot/u-boot.inc
inherit pythonnative

PROVIDES += "u-boot"
DEPENDS_append = " python dtc-native"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/gpl-2.0.txt;md5=b234ee4d69f5fce4486a80fdaf4a4263"

UBOOT_SRC ?= "git://git.toradex.com/u-boot-toradex.git;protocol=https"
SRCBRANCH = "toradex_imx_v2017.03_4.9.51_imx8_beta1-bring_up"
SRC_URI = "${UBOOT_SRC};branch=${SRCBRANCH}"
SRCREV = "5a893f3d535784952deb2a901681c2d1ed0f877e"

S = "${WORKDIR}/git"

inherit fsl-u-boot-localversion

LOCALVERSION ?= "-imx_4.9.51_8qm_beta1_toradex"

PACKAGE_ARCH = "${MACHINE_ARCH}"
COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"

UBOOT_NAME_mx6 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
UBOOT_NAME_mx7 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
UBOOT_NAME_mx8 = "u-boot-${MACHINE}.bin-${UBOOT_CONFIG}"
