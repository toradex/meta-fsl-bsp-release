FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_append_imxgpu2d = " virtual/libg2d"
DEPENDS_append_mx7ulp = " libdrm"

PACKAGECONFIG_GL_imxgpu2d = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl x11', 'opengl', '', d)}"
PACKAGECONFIG_GL_imxgpu3d = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2', '', d)}"

PACKAGECONFIG_append_mx6q = " opencv"
PACKAGECONFIG_append_mx6qp = " opencv"
PACKAGECONFIG_append_mx8 = " opencv"
PACKAGECONFIG_remove_mx6sl = " gles2"

#revert poky fido commit:cdc2c8aeaa96b07dfc431a4cf0bf51ef7f8802a3: move EGL to Wayland
PACKAGECONFIG[gles2]   = "--enable-gles2 --enable-egl,--disable-gles2 --disable-egl,virtual/libgles2 virtual/egl"
PACKAGECONFIG[wayland] = "--enable-wayland --disable-x11,--disable-wayland,wayland"

# Disable introspection to fix [GstGL-1.0.gir] Error 
EXTRA_OECONF_append = " --disable-introspection"

EXTRA_OECONF_remove = " --disable-sdl --disable-nas --disable-libvisual --disable-xvid --disable-mimic \
                        --disable-pvr --disable-sdltest --disable-wininet --disable-timidity \
                        --disable-linsys --disable-sndio --disable-apexsink \
"
# Use i.MX fork of GST for customizations
SRC_URI_remove_imx = " \
    http://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz \
"

GST1.0-PLUGINS-BAD_SRC ?= "gitsm://source.codeaurora.org/external/imx/gst-plugins-bad.git;protocol=https"
SRCBRANCH = "MM_04.03.03_1712_L4.9.51_MX8_BETA2"

SRC_URI_append_imx = " \
    ${GST1.0-PLUGINS-BAD_SRC};branch=${SRCBRANCH} \
    file://0001-gst-plugins-bad-Correct-PKG_COFING-directory-for-WAY.patch \
    file://0001-gstreamer-gl.pc.in-Don-t-append-GL_CFLAGS-to-CFLAGS.patch \
"

SRCREV_imx = "157eccff4a90cbe83baae1120fb9f5ee658269be"

# This remove "--exclude=autopoint" option from autoreconf argument to avoid
# configure.ac:30: error: required file './ABOUT-NLS' not found
EXTRA_AUTORECONF = ""

# include fragment shaders
FILES_${PN}-opengl += "/usr/share/*.fs"

PACKAGE_ARCH_imxpxp = "${MACHINE_SOCARCH}"
PACKAGE_ARCH_mx8 = "${MACHINE_SOCARCH}"

# Fix libgstbadion-1.0.so.0 which is under built directory cannot be found
do_compile_prepend () {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/ion/.libs"
}

S_imx = "${WORKDIR}/git"
