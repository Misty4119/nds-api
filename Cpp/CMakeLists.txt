cmake_minimum_required(VERSION 3.25)
project(noie_nds_api_proto LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 26)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Protobuf dependency strategy.
# - system: use a pre-installed Protobuf CMake package.
# - fetch:  FetchContent from upstream protobuf git (hermetic / avoids distro old headers).
set(NDS_API_PROTOBUF_PROVIDER "fetch" CACHE STRING "Protobuf provider: system|fetch")
set_property(CACHE NDS_API_PROTOBUF_PROVIDER PROPERTY STRINGS system fetch)

# Generated sources live under Cpp/src/** (protobuf C++ generator layout).
file(GLOB_RECURSE NDS_PROTO_SOURCES CONFIGURE_DEPENDS
  "${CMAKE_CURRENT_LIST_DIR}/src/*.pb.cc"
)

add_library(noie_nds_api_proto ${NDS_PROTO_SOURCES})
target_include_directories(noie_nds_api_proto PUBLIC "${CMAKE_CURRENT_LIST_DIR}/src")

if(NDS_API_PROTOBUF_PROVIDER STREQUAL "system")
  find_package(Protobuf CONFIG REQUIRED)
else()
  include(FetchContent)
  # Use an established stable tag that contains runtime_version.h (introduced in 26.x).
  set(NDS_API_PROTOBUF_TAG "v33.4" CACHE STRING "protobuf git tag for FetchContent")

  # Keep protobuf build lightweight.
  set(protobuf_BUILD_TESTS OFF CACHE BOOL "" FORCE)
  set(protobuf_BUILD_CONFORMANCE OFF CACHE BOOL "" FORCE)
  set(protobuf_BUILD_EXAMPLES OFF CACHE BOOL "" FORCE)
  set(protobuf_BUILD_PROTOC_BINARIES OFF CACHE BOOL "" FORCE)

  FetchContent_Declare(protobuf
    GIT_REPOSITORY https://github.com/protocolbuffers/protobuf.git
    GIT_TAG "${NDS_API_PROTOBUF_TAG}"
    GIT_SHALLOW TRUE
    SOURCE_SUBDIR cmake
  )
  FetchContent_MakeAvailable(protobuf)
endif()

# When building protobuf via FetchContent, the CMake targets are already available.
# When using the system provider, `find_package(Protobuf CONFIG REQUIRED)` should
# provide `protobuf::libprotobuf`.
if(TARGET protobuf::libprotobuf)
  target_link_libraries(noie_nds_api_proto PUBLIC protobuf::libprotobuf)
elseif(TARGET libprotobuf)
  target_link_libraries(noie_nds_api_proto PUBLIC libprotobuf)
else()
  message(FATAL_ERROR "Protobuf target not found (expected protobuf::libprotobuf or libprotobuf).")
endif()

include(GNUInstallDirs)
install(TARGETS noie_nds_api_proto
  EXPORT noie_nds_api_protoTargets
  ARCHIVE DESTINATION "${CMAKE_INSTALL_LIBDIR}"
  LIBRARY DESTINATION "${CMAKE_INSTALL_LIBDIR}"
  RUNTIME DESTINATION "${CMAKE_INSTALL_BINDIR}"
)
install(DIRECTORY "${CMAKE_CURRENT_LIST_DIR}/src/" DESTINATION "${CMAKE_INSTALL_INCLUDEDIR}")

