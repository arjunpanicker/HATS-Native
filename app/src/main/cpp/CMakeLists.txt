# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.10.2)

# Declares and names the project.

project(Hats_ML VERSION 1.0 DESCRIPTION "ML Module for HATS")

set(SOURCE_FILES
        ft/src/args.cpp
        ft/src/autotune.cpp
        ft/src/densematrix.cpp
        ft/src/dictionary.cpp
        ft/src/Fasttext.cpp
        ft/src/loss.cpp
        ft/src/matrix.cpp
        ft/src/meter.cpp
        ft/src/model.cpp
        ft/src/productquantizer.cpp
        ft/src/quantmatrix.cpp
        ft/src/utils.cpp
        ft/src/vector.cpp

        preprocessing/src/datasethandler.cpp
        preprocessing/src/preprocessing.cpp
        preprocessing/src/embedding.cpp

        ml/src/nnet.cpp
        ml/src/ml_utils.cpp

        main.cpp
        )

find_package (Threads)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

#add_library( # Sets the name of the library.
#             Hats_ML
#
#             # Sets the library as a shared library.
#             SHARED
#
#             # Provides a relative path to your source file(s).
#             native-lib.cpp )

add_library(Hats_ML SHARED ${SOURCE_FILES})

target_include_directories(Hats_ML PUBLIC
        "${PROJECT_SOURCE_DIR}/ft/include"
        "${PROJECT_SOURCE_DIR}/preprocessing/include"
        "${PROJECT_SOURCE_DIR}/ml/include"
        )
message("Creating shared library")

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       Hats_ML

                       # Links the target library to the log library
                       # included in the NDK.
                       PUBLIC ${log-lib}
                       PUBLIC ${CMAKE_THREAD_LIBS_INIT} )

set_target_properties(Hats_ML PROPERTIES VERSION ${PROJECT_VERSION})
