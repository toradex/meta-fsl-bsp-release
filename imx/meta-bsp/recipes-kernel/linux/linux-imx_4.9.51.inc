# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "imx_4.9.51_imx8_beta2"

LOCALVERSION = "-${SRCBRANCH}"

KERNEL_SRC ?= "git://source.codeaurora.org/external/imx/linux-imx.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "9d26caaf2e532f4b02e695e6f2ad31ad493f2add"

S = "${WORKDIR}/git"
