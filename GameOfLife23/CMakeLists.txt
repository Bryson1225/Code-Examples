cmake_minimum_required(VERSION 3.21)

project(GameOfLife23)

set(CMAKE_CXX_STANDARD 14)

# TODO: Change this to your path to catch.cpp
include_directories(/Users/mster/CLionProjects/GameOfLife23)

add_executable(GameOfLife23 GameOfLife.cpp GameOfLifeTest.cpp)

# Use this to run the main method
#add_executable(GameOfLife23 main.cpp)
