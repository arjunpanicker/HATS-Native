set(ML_FILES
    src/nnet.cpp
    src/ml_utils.cpp
)

set(ML_HEADER_FILES
    include/nnet.h
    include/ml_utils.h
)

add_library(ml ${ML_FILES})

target_include_directories(ml
    PUBLIC include
    INTERFACE ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(ml PUBLIC preprocessing)
