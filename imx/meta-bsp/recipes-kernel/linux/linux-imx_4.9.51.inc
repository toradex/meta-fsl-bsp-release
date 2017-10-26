# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "imx_4.9.51_imx8_beta1"

LOCALVERSION = "-${SRCBRANCH}"

KERNEL_SRC ?= "git://source.codeaurora.org/external/imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "1f243732a5e9a6a8adeb02fa0e93ea60fdba85bf"

S = "${WORKDIR}/git"
