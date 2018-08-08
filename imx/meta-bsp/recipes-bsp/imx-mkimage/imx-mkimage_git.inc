# Copyright 2017-2018 NXP

IMX_MKIMAGE_SRC ?= "git://source.codeaurora.org/external/imx/imx-mkimage.git;protocol=https"
SRCBRANCH = "imx_4.14.78_1.0.0_ga"
SRC_URI = "${IMX_MKIMAGE_SRC};branch=${SRCBRANCH}"
SRCREV = "2cf091c075ea1950afa22a56e224dc4e448db542"

SRC_URI += " \
    file://0001-add-board-support-for-DCD-memory-timings.patch \
    file://0002-add-memory-timings-for-Apalis-iMX8.patch \
    file://0001-mx8x-add-board-support-for-DCD-memory-timings.patch \
    file://0002-imx8x-add-memory-timings-for-Colibri-iMX8X.patch \
"

DEPENDS = "zlib-native openssl-native"
