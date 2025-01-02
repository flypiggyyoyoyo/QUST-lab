cmake_minimum_required(VERSION 3.30)
project(MiniGps)

set(CMAKE_CXX_STANDARD 23)

include_directories(include)

link_libraries(libeasyx.a)

add_executable(MiniGps src/main.cpp
        src/ui.cpp
        src/graph.cpp
        include/ui.h
        include/graph.h
)
