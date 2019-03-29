# Copyright 2017-2018 NXP
# Copyright 2018 Toradex AG
# Released under the MIT license (see COPYING.MIT for the terms)

SRCBRANCH = "toradex_imx_4.14.78_1.0.0_ga-bring_up"

LOCALVERSION = "-${SRCBRANCH}"

KERNEL_SRC ?= "git://git.toradex.com/linux-toradex.git;protocol=https"
SRC_URI = "${KERNEL_SRC};branch=${SRCBRANCH}"
SRCREV = "72711ba7c0904fecd514151a92b04fce93c4624d"
# SRCREV = "${AUTOREV}"
