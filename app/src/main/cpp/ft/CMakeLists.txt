set(FT_FILES
    src/args.cpp
    src/autotune.cpp
    src/densematrix.cpp
    src/dictionary.cpp
    src/Fasttext.cpp
    src/loss.cpp
    src/matrix.cpp
    src/meter.cpp
    src/model.cpp
    src/productquantizer.cpp
    src/quantmatrix.cpp
    src/utils.cpp
    src/vector.cpp
)

set(FT_HEADER_FILES
    include/args.h
    include/autotune.h
    include/config.h
    include/densematrix.h
    include/dictionary.h
    include/Fasttext.h
    include/loss.h
    include/matrix.h
    include/meter.h
    include/model.h
    include/productquantizer.h
    include/quantmatrix.h
    include/utils.h
    include/vector.h
)

add_library(ft ${FT_FILES})

target_include_directories(ft
    PUBLIC include
)

target_include_directories(ft
    INTERFACE ${CMAKE_CURRENT_SOURCE_DIR}
)