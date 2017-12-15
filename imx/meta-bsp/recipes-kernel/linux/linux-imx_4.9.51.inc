# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "imx_4.9.51_imx8_beta2"

LOCALVERSION = "-${SRCBRANCH}"

KERNEL_SRC ?= "git://source.codeaurora.org/external/imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "679da391c069db2620835cad11dbe8ca62b75bda"

S = "${WORKDIR}/git"
