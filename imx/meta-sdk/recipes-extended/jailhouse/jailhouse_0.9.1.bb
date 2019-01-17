require jailhouse.inc

SRCBRANCH = "imx_4.14.78_1.0.0_ga"
IMX_JAILHOUSE_SRC ?= "git://source.codeaurora.org/external/imx/imx-jailhouse.git;protocol=ssh"

SRC_URI = "${IMX_JAILHOUSE_SRC};branch=${SRCBRANCH}"
SRCREV = "126566e9de4371fc8013e976518c7c60e1425508"

CELLS = ""

COMPATIBLE_MACHINE = "${@bb.utils.contains('MACHINE_FEATURES', 'jailhouse', '${MACHINE}', '(^$)', d)}"
