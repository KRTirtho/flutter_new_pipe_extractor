# The Flutter tooling requires that developers have a version of Visual Studio
# installed that includes CMake 3.14 or later. You should not increase this
# version, as doing so will cause the plugin to fail to compile for some
# customers of the plugin.
cmake_minimum_required(VERSION 3.14)

# Project-level configuration.
set(PROJECT_NAME "flutter_new_pipe_extractor")
project(${PROJECT_NAME} LANGUAGES CXX)

# Explicitly opt in to modern CMake behaviors to avoid warnings with recent
# versions of CMake.
cmake_policy(VERSION 3.14...3.25)

# This value is used when generating builds using this plugin, so it must
# not be changed
set(PLUGIN_NAME "flutter_new_pipe_extractor_plugin")

# Any new source files that you add to the plugin should be added here.
list(APPEND PLUGIN_SOURCES
  "flutter_new_pipe_extractor_plugin.cpp"
  "flutter_new_pipe_extractor_plugin.h"
)

# Define the plugin library target. Its name must not be changed (see comment
# on PLUGIN_NAME above).
add_library(${PLUGIN_NAME} SHARED
  "include/flutter_new_pipe_extractor/flutter_new_pipe_extractor_plugin_c_api.h"
  "flutter_new_pipe_extractor_plugin_c_api.cpp"
  ${PLUGIN_SOURCES}
)

# Apply a standard set of build settings that are configured in the
# application-level CMakeLists.txt. This can be removed for plugins that want
# full control over build settings.
apply_standard_settings(${PLUGIN_NAME})

# Symbols are hidden by default to reduce the chance of accidental conflicts
# between plugins. This should not be removed; any symbols that should be
# exported should be explicitly exported with the FLUTTER_PLUGIN_EXPORT macro.
set_target_properties(${PLUGIN_NAME} PROPERTIES
  CXX_VISIBILITY_PRESET hidden)
target_compile_definitions(${PLUGIN_NAME} PRIVATE FLUTTER_PLUGIN_IMPL)

# Source include directories and library dependencies. Add any plugin-specific
# dependencies here.
target_include_directories(${PLUGIN_NAME} INTERFACE
  "${CMAKE_CURRENT_SOURCE_DIR}/include")
target_link_libraries(${PLUGIN_NAME} PRIVATE flutter flutter_wrapper_plugin)

# === Download NewPipeCLI binary for Windows with SHA256 check ===
set(ASSETS_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../assets")
file(MAKE_DIRECTORY ${ASSETS_DIR})

set(NEWPIPECLI_ZIP "${ASSETS_DIR}/NewPipeCLI-windows-x64.zip")
set(NEWPIPECLI_URL "https://github.com/KRTirtho/NewPipeCLI/releases/download/v0.1.0/NewPipeCLI-windows-x64.zip")

# Expected SHA256 checksum of the release zip
set(NEWPIPECLI_SHA256 "b8e0f0754f7c612197630511130390bd7ea6348c39f49ae6be2b394717600199")

# Default: need to download
set(SHOULD_DOWNLOAD TRUE)

# If zip exists, verify checksum
if(EXISTS ${NEWPIPECLI_ZIP})
  file(SHA256 ${NEWPIPECLI_ZIP} EXISTING_SHA256)
  if(EXISTING_SHA256 STREQUAL NEWPIPECLI_SHA256)
    message(STATUS "NewPipeCLI zip already exists and verified.")
    set(SHOULD_DOWNLOAD FALSE)
  else()
    message(WARNING "Existing NewPipeCLI zip checksum mismatch, re-downloading...")
  endif()
endif()

# Download if missing or invalid
if(SHOULD_DOWNLOAD)
  message(STATUS "Downloading NewPipeCLI for Windows...")
  file(DOWNLOAD ${NEWPIPECLI_URL} ${NEWPIPECLI_ZIP} SHOW_PROGRESS)
  
  # Verify checksum after download
  file(SHA256 ${NEWPIPECLI_ZIP} DOWNLOADED_SHA256)
  if(NOT DOWNLOADED_SHA256 STREQUAL NEWPIPECLI_SHA256)
    message(FATAL_ERROR "Downloaded NewPipeCLI zip checksum mismatch! Aborting build.")
  endif()
endif()

# List of absolute paths to libraries that should be bundled with the plugin.
# This list could contain prebuilt libraries, or libraries created by an
# external build triggered from this build file.
set(flutter_new_pipe_extractor_bundled_libraries
  ""
  PARENT_SCOPE
)
