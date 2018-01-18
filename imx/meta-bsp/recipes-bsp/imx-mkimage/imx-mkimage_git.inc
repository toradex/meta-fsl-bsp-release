# Copyright 2017-2018 NXP

IMX_MKIMAGE_SRC ?= "git://source.codeaurora.org/external/imx/imx-mkimage.git;protocol=https"
SRCBRANCH = "imx_4.9.123_imx8mm_ga"
SRC_URI = "${IMX_MKIMAGE_SRC};branch=${SRCBRANCH}"
SRCREV = "9e289f314eddb9d38cab73780b936acb30bb88b4"

SRC_URI += " \
    file://0001-add-board-support-for-DCD-memory-timings.patch \
    file://0002-add-memory-timings-for-Apalis-iMX8.patch \
"

DEPENDS = "zlib-native openssl-native"
