# Copyright (C) 2013-2016 Freescale Semiconductor
# Copyright 2017 NXP

include recipes-bsp/imx-test/imx-test.inc

PV = "7.0+${SRCPV}"

SRCBRANCH = "imx_4.9.51_imx8_beta2"
IMXTEST_SRC ?= "git://source.codeaurora.org/external/imx/imx-test.git;protocol=https"
SRC_URI = "${IMXTEST_SRC};branch=${SRCBRANCH}"
SRC_URI_append = " file://memtool_profile "

SRCREV = "867f37a7aca098b1ca8e3ca78f3b7c182788f66e"

S = "${WORKDIR}/git"

DEPENDS_append        = " alsa-lib"
DEPENDS_append_mx7ulp = " virtual/kernel imx-lib"
DEPENDS_append_mx8    = " virtual/kernel"
DEPENDS_append_imxvpu = " virtual/imxvpu"

# We include VPU for certain parts that don't have VPU
# https://bitbucket.sw.nxp.com/projects/IMX/repos/meta-fsl-bsp-release/commits/d03342173b6eec922d015230c8b28fc579034ff1#imx/meta-bsp/recipes-bsp/imx-vpu/imx-vpu_5.4.32.bb
DEPENDS_append_mx6sl  = " virtual/imxvpu"
DEPENDS_append_mx6sx  = " virtual/imxvpu"
DEPENDS_append_mx6ul  = " virtual/imxvpu"
DEPENDS_append_mx6sll = " virtual/imxvpu"
DEPENDS_append_mx7d   = " virtual/imxvpu"

PLATFORM_mx6sll = "IMX6SL"
PLATFORM_mx7ulp  = "IMX7D"
PLATFORM_mx8 = "IMX8"

IMX_HAS_VPU         = "false"
IMX_HAS_VPU_imxvpu  = "true"
EXTRA_OEMAKE       += "HAS_VPU=${IMX_HAS_VPU}"

PARALLEL_MAKE="-j 1"

do_install_append() {
    install -d -m 0755 ${D}/home/root/
    install -m 0644 ${WORKDIR}/memtool_profile ${D}/home/root/.profile
}

FILES_${PN} += " /home/root/.profile "

COMPATIBLE_MACHINE = "(mx6|mx7|mx8)"
