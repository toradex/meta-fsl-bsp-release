# Copyright 2017 NXP

IMX_MKIMAGE_SRC ?= "git://source.codeaurora.org/external/imx/imx-mkimage.git;protocol=https"
SRCBRANCH ?= "imx_4.9.51_imx8_beta2"
SRC_URI = " \
    ${IMX_MKIMAGE_SRC};branch=${SRCBRANCH} \
    file://0001-add-board-support-for-DCD-memory-timings.patch \
    file://0002-add-memory-timings-for-Apalis-iMX8.patch \
"


SRCREV = "4352f717fe643c1aaed3feb55931d92d3b83027a"
