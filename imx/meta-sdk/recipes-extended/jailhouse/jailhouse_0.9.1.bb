require jailhouse.inc

SRCBRANCH = "imx_4.14.78_1.0.0_ga"
IMX_JAILHOUSE_SRC ?= "git://source.codeaurora.org/external/imx/imx-jailhouse.git;protocol=ssh"

SRC_URI = "${IMX_JAILHOUSE_SRC};branch=${SRCBRANCH}"
SRCREV = "7ce612e3f7db3f8e1b4ee0b674dba09c4561c7fa"

CELLS = ""

COMPATIBLE_MACHINE = "${@bb.utils.contains('MACHINE_FEATURES', 'jailhouse', '${MACHINE}', '(^$)', d)}"
