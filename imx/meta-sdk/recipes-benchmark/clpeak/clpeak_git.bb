SUMMARY = "A tool which profiles OpenCL devices to find their peak capacities"
HOMEPAGE = "https://github.com/krrishnarraj/clpeak"
LICENSE = "PD"
LIC_FILES_CHKSUM = "file://LICENSE;md5=911690f51af322440237a253d695d19f"
DEPENDS = "virtual/opencl-icd"

SRC_URI = " \
    git://github.com/krrishnarraj/clpeak.git;protocol=https \
    file://0001-CMakeLists-add-install-rule.patch \
"

SRCREV = "8edab23fbc867adbada21378d65774c670c2aaf9"
S = "${WORKDIR}/git"

inherit cmake
