set(PREPROCESSING_FILES
    src/datasethandler.cpp
    src/preprocessing.cpp
    src/embedding.cpp
)

set(PREPROCESSING_HEADER_FILES
    include/real.h
    include/pre_utils.h
    include/datasethandler.h
    include/preprocessing.h
    include/embedding.h
)

add_library(preprocessing ${PREPROCESSING_FILES})

target_include_directories(preprocessing
    PUBLIC include
    INTERFACE ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(preprocessing PUBLIC ft)
