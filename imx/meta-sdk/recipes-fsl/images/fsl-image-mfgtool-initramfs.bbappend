# Replace linux-mfgtool with virtual/kernel

DEPENDS_remove = "linux-mfgtool"
DEPENDS_append = " virtual/kernel"
PACKAGE_ARCH = "${MACHINE_ARCH}"
