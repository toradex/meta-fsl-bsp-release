SUMMARY = "CPU burn app that loads the NEON coprocessor fully"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://cpuburn-a53.S;md5=a32d75f7e1fa9afbb887bef71d89875a"

DL_DIR_append = "/${PN}-${PV}"

# Ensure to make this available for machine which has neon
COMPATIBLE_MACHINE = "(${@bb.utils.contains("TUNE_FEATURES", "aarch64", "${MACHINE}", "Invalid!", d)})"

SRC_URI = "http://hardwarebug.org/files/burn.S;name=mru \
           https://raw.githubusercontent.com/ssvb/cpuburn-arm/dd5c5ba58d2b0b23cfab4a286f9d3f5510000f20/cpuburn-a8.S;name=ssvb \
           https://raw.githubusercontent.com/ssvb/cpuburn-arm/ad7e646700d14b81413297bda02fb7fe96613c3f/cpuburn-a53.S;name=ssvb53 \
"

SRC_URI[mru.md5sum] = "823abc72c2cd448e87df9bc5355a4456"
SRC_URI[mru.sha256sum] = "01d9fc04f83740c513c25401dcc89c11b2a5a6013e70bfca42b7b02129f88cd2"
SRC_URI[ssvb.md5sum] = "ba0ef2939a3b3b487523448c67544e94"
SRC_URI[ssvb.sha256sum] = "ce42ebdc71c876a33d9f7534355ef76cefa0d00ddb19ad69cf05a266c861d08d"
SRC_URI[ssvb53.md5sum] = "a32d75f7e1fa9afbb887bef71d89875a"
SRC_URI[ssvb53.sha256sum] = "502b3a17186da34976cb97d7ae2083a66c42cddaffc1e900a4dd23efc64d97f1"


S = "${WORKDIR}"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} burn.S -o burn
    ${CC} ${CFLAGS} ${LDFLAGS} cpuburn-a8.S -o burn-neona8
}

do_compile_aarch64() {
    ${CC} ${CFLAGS} ${LDFLAGS} --static cpuburn-a53.S -o burn-a53
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/burn ${D}${bindir}/burn-neon
    install -m 0755 ${S}/burn-neona8 ${D}${bindir}/
}

do_install_aarch64() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/burn-a53 ${D}${bindir}/
}

