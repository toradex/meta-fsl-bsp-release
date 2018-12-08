# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP
# Released under the MIT license (see COPYING.MIT for the terms)

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

#SRC_URI_append = " file://Install-dma-buf-h.patch"

inherit fsl-vivante-kernel-driver-handler
