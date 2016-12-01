cmake_minimum_required(VERSION 3.6)
project(cuda)

set ( EIGEN3_FOUND 1 )
set ( EIGEN3_USE_FILE     "${CMAKE_CURRENT_LIST_DIR}/UseEigen3.cmake" )

set ( EIGEN3_DEFINITIONS  "" )
set ( EIGEN3_INCLUDE_DIR  "/usr/local/Cellar/eigen/3.3.0/include/eigen3" )
set ( EIGEN3_INCLUDE_DIRS "/usr/local/Cellar/eigen/3.3.0/include/eigen3" )
set ( EIGEN3_ROOT_DIR     "/usr/local/Cellar/eigen/3.3.0" )

set ( EIGEN3_VERSION_STRING "3.3.0" )
set ( EIGEN3_VERSION_MAJOR  "3" )
set ( EIGEN3_VERSION_MINOR  "3" )
set ( EIGEN3_VERSION_PATCH  "0" )

set ( CMAKE_CXX_STANDARD 11 )

find_package(OpenMP)
if (OPENMP_FOUND)
    set (CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${OpenMP_C_FLAGS}")
    set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
endif()

set(SOURCE_FILES main.cpp CTMatrix.cpp CTMatrix.h PPMWriter.cpp PPMWriter.h SIRT.cpp SIRT.h SART.cpp SART.h)

add_definitions     ( ${EIGEN3_DEFINITIONS} )
include_directories ( ${EIGEN3_INCLUDE_DIRS} )

add_executable(cuda ${SOURCE_FILES})