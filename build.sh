#set -x

TOP=GCD
BUILD_DIR=obj_dir

echo "Building ${TOP}..."
rm -rf ${BUILD_DIR}
mkdir -p ${BUILD_DIR}
verilator --cc --exe --main --timing $1
cp ./VGCD__main.cpp ${BUILD_DIR}
#--prof-cfuncs
make -C ${BUILD_DIR} -f V${TOP}.mk -j16 -s
echo "Running ./${BUILD_DIR}/V${TOP}..."
./${BUILD_DIR}/V${TOP}

