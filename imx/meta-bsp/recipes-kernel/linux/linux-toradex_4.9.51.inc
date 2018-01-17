# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "toradex_imx_4.9.51_imx8_beta1-bring_up"

LOCALVERSION = "-imx_4.9.51_8qm_beta1_toradex"

KERNEL_SRC ?= "git://git.toradex.com/linux-toradex.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "89d5dc3711c1840f340bc31542ff24cc447898d2"

S = "${WORKDIR}/git"
