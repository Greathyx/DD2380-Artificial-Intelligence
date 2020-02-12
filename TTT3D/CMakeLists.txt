cmake_minimum_required(VERSION 3.14)
project(TTT3D)

set(CMAKE_CXX_STANDARD 14)

include_directories(.)

add_executable(TTT3D
        constants.hpp
        deadline.hpp
        gamestate.cpp
        gamestate.hpp
        main.cpp
        move.hpp
        player.cpp
        player.hpp)