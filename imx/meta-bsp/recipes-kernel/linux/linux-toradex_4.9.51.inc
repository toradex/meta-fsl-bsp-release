# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "toradex_imx_4.9.51_imx8_beta2-bring_up"

LOCALVERSION = "-imx_4.9.51_8qm_beta2_toradex"

KERNEL_SRC ?= "git://git.toradex.com/linux-toradex.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "a1a1244b1de00274586d1ea16048f8469966ff30"

S = "${WORKDIR}/git"
