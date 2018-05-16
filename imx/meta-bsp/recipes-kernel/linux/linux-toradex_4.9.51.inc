# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "toradex_imx_4.9.51_imx8_beta2-bring_up"

LOCALVERSION = "-imx_4.9.51_8qm_beta2_toradex"

KERNEL_SRC ?= "git://git.toradex.com/linux-toradex.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "6be9d4ba6f9e4bbb27b2ae4ba5be949183cab8bd"

S = "${WORKDIR}/git"
