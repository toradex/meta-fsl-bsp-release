# Copyright 2017 NXP

IMX_MKIMAGE_SRC ?= "git://source.codeaurora.org/external/imx/imx-mkimage.git;protocol=https"
SRCBRANCH ?= "imx_4.9.51_imx8_beta2"
SRC_URI = "${IMX_MKIMAGE_SRC};branch=${SRCBRANCH}"
SRCREV = "f7079d53992f3d9f440019c6ef53f10691fe29a6"
