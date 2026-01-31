cmake_minimum_required(VERSION 3.25)
project(noie_nds_api_proto LANGUAGES CXX)

set(CMAKE_CXX_STANDARD 26)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Generated sources live under Cpp/src/** (protobuf C++ generator layout).
file(GLOB_RECURSE NDS_PROTO_SOURCES CONFIGURE_DEPENDS
  "${CMAKE_CURRENT_LIST_DIR}/src/*.pb.cc"
)

add_library(noie_nds_api_proto ${NDS_PROTO_SOURCES})
target_include_directories(noie_nds_api_proto PUBLIC "${CMAKE_CURRENT_LIST_DIR}/src")

find_package(Protobuf CONFIG REQUIRED)
target_link_libraries(noie_nds_api_proto PUBLIC protobuf::libprotobuf)

include(GNUInstallDirs)
install(TARGETS noie_nds_api_proto
  EXPORT noie_nds_api_protoTargets
  ARCHIVE DESTINATION "${CMAKE_INSTALL_LIBDIR}"
  LIBRARY DESTINATION "${CMAKE_INSTALL_LIBDIR}"
  RUNTIME DESTINATION "${CMAKE_INSTALL_BINDIR}"
)
install(DIRECTORY "${CMAKE_CURRENT_LIST_DIR}/src/" DESTINATION "${CMAKE_INSTALL_INCLUDEDIR}")

