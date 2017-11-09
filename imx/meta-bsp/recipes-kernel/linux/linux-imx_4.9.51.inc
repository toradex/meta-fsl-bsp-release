# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "imx_4.9.51_imx8_beta1"

LOCALVERSION = "-imx_4.9.51_8qm_beta1_8qxp_alpha"

KERNEL_SRC ?= "git://source.codeaurora.org/external/imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "bc10a883e211228c28e402c99eece57a830e6d51"

S = "${WORKDIR}/git"
