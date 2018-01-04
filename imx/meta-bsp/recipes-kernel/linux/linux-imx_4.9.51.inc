# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "imx_4.9.51_imx8_beta2"

LOCALVERSION = "-8qm_beta2_8qxp_beta"

KERNEL_SRC ?= "git://source.codeaurora.org/external/imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "668f73ed73a697ab2f06db97902a2a67bfd4031a"

S = "${WORKDIR}/git"
