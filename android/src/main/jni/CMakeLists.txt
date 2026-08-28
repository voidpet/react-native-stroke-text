cmake_minimum_required(VERSION 3.13)
set(CMAKE_VERBOSE_MAKEFILE ON)
set(CMAKE_CXX_STANDARD 20)

set(LIB_LITERAL RNStrokeText)
set(LIB_TARGET_NAME react_codegen_${LIB_LITERAL})

set(LIB_ANDROID_DIR ${CMAKE_CURRENT_SOURCE_DIR}/../../..)
set(LIB_COMMON_DIR ${LIB_ANDROID_DIR}/../common/cpp)
set(LIB_CUSTOM_SOURCES_DIR ${LIB_COMMON_DIR}/react/renderer/components/${LIB_LITERAL})
set(LIB_ANDROID_GENERATED_JNI_DIR ${LIB_ANDROID_DIR}/build/generated/source/codegen/jni)
set(LIB_ANDROID_GENERATED_COMPONENTS_DIR ${LIB_ANDROID_GENERATED_JNI_DIR}/react/renderer/components/RNStrokeTextSpec)

file(GLOB LIB_CODEGEN_SRCS CONFIGURE_DEPENDS
  ${LIB_ANDROID_GENERATED_JNI_DIR}/*.cpp
  ${LIB_ANDROID_GENERATED_COMPONENTS_DIR}/*.cpp
)
file(GLOB LIB_CUSTOM_SRCS CONFIGURE_DEPENDS ${LIB_CUSTOM_SOURCES_DIR}/*.cpp)

add_library(
  ${LIB_TARGET_NAME}
  SHARED
  ${LIB_CODEGEN_SRCS}
  ${LIB_CUSTOM_SRCS}
)

target_include_directories(
  ${LIB_TARGET_NAME}
  PUBLIC
  .
  ${LIB_COMMON_DIR}
  ${LIB_ANDROID_GENERATED_JNI_DIR}
  ${LIB_ANDROID_GENERATED_COMPONENTS_DIR}
)

find_package(fbjni REQUIRED CONFIG)
find_package(ReactAndroid REQUIRED CONFIG)

# React Native 0.81's merged prefab omits these platform-specific text header
# directories from the main target's include path.
if(ReactAndroid_VERSION_MINOR GREATER_EQUAL 81)
  get_target_property(REACT_NATIVE_INCLUDE_DIRS ReactAndroid::reactnative INTERFACE_INCLUDE_DIRECTORIES)
  foreach(INCLUDE_DIR ${REACT_NATIVE_INCLUDE_DIRS})
    if(EXISTS "${INCLUDE_DIR}/react/renderer/components/text/platform/android")
      target_include_directories(
        ${LIB_TARGET_NAME}
        PUBLIC
        ${INCLUDE_DIR}/react/renderer/components/text/platform/android
      )
    endif()
    if(EXISTS "${INCLUDE_DIR}/react/renderer/components/text/platform/cxx")
      target_include_directories(
        ${LIB_TARGET_NAME}
        PUBLIC
        ${INCLUDE_DIR}/react/renderer/components/text/platform/cxx
      )
    endif()
  endforeach()
endif()

target_link_libraries(
  ${LIB_TARGET_NAME}
  fbjni::fbjni
  ReactAndroid::jsi
  ReactAndroid::reactnative
)

if(ReactAndroid_VERSION_MINOR GREATER_EQUAL 80)
  target_compile_reactnative_options(${LIB_TARGET_NAME} PUBLIC)
else()
  target_compile_options(
    ${LIB_TARGET_NAME}
    PRIVATE
    -fexceptions
    -frtti
    -std=c++20
    -Wall
  )
endif()

target_include_directories(
  ${CMAKE_PROJECT_NAME}
  PUBLIC
  ${CMAKE_CURRENT_SOURCE_DIR}
)
