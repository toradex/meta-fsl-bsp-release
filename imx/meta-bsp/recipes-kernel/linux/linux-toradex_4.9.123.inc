# Copyright 2017-2018 NXP
# Copyright 2018 Toradex AG
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "toradex_imx_4.9.123_imx8mm_ga-bring_up"

LOCALVERSION = "-${SRCBRANCH}"

KERNEL_SRC ?= "git://git.toradex.com/linux-toradex.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "bfbd5dcc6088601c13fe07cc69b1c88994867d8a"
