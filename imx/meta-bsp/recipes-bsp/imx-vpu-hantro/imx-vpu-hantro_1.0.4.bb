# Copyright 2017 NXP

require imx-vpu-hantro.inc
LIC_FILES_CHKSUM = "file://COPYING;md5=08fd295cce89b0a9c74b9b83ed74f671"

SRC_URI[md5sum] = "a0b4ec72ef7a5d7d57e3727e4c6e374f"
SRC_URI[sha256sum] = "f88eb700cb54f971dc08c33567d2861773237139fc9d8566ac73e5071e510986"

# Compatible only for i.MX with Hantro VPU
COMPATIBLE_MACHINE = "(^$)"
COMPATIBLE_MACHINE_imxvpuhantro = "${MACHINE}"