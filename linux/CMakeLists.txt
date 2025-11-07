# The Flutter tooling requires that developers have CMake 3.10 or later
# installed. You should not increase this version, as doing so will cause
# the plugin to fail to compile for some customers of the plugin.
cmake_minimum_required(VERSION 3.11)

# Project-level configuration.
set(PROJECT_NAME "flutter_new_pipe_extractor")
project(${PROJECT_NAME} LANGUAGES CXX)

# This value is used when generating builds using this plugin, so it must
# not be changed.
set(PLUGIN_NAME "flutter_new_pipe_extractor_plugin")

# Any new source files that you add to the plugin should be added here.
list(APPEND PLUGIN_SOURCES
  "flutter_new_pipe_extractor_plugin.cc"
)

# Define the plugin library target. Its name must not be changed (see comment
# on PLUGIN_NAME above).
add_library(${PLUGIN_NAME} SHARED
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
target_link_libraries(${PLUGIN_NAME} PRIVATE flutter)
target_link_libraries(${PLUGIN_NAME} PRIVATE PkgConfig::GTK)

# === Download NewPipeCLI binary ===
set(ASSETS_DIR "${CMAKE_CURRENT_SOURCE_DIR}/../assets")
file(MAKE_DIRECTORY ${ASSETS_DIR})

# Detect system architecture
string(TOLOWER "${CMAKE_SYSTEM_PROCESSOR}" ARCH)
if(ARCH MATCHES "aarch64" OR ARCH MATCHES "arm64")
  set(NEWPIPECLI_FILE "NewPipeCLI-linux-arm64.zip")
  set(NEWPIPECLI_SHA256 "1da4cea8e2b7daec616c7b17334182fcc5969e9b435a176549845fb56a1475d4")
else()
  set(NEWPIPECLI_FILE "NewPipeCLI-linux-x64.zip")
  set(NEWPIPECLI_SHA256 "9712f05be02959d3e7f659c025306f34d1162f5f965fd70a576c0ca35160e882")
endif()

set(NEWPIPECLI_ZIP "${ASSETS_DIR}/${NEWPIPECLI_FILE}")
set(NEWPIPECLI_URL "https://github.com/KRTirtho/NewPipeCLI/releases/download/v0.1.0/${NEWPIPECLI_FILE}")

# Default: need to download
set(SHOULD_DOWNLOAD TRUE)

# Verify checksum if file exists
if(EXISTS ${NEWPIPECLI_ZIP})
  file(SHA256 ${NEWPIPECLI_ZIP} EXISTING_SHA256)
  if(EXISTING_SHA256 STREQUAL NEWPIPECLI_SHA256)
    message(STATUS "NewPipeCLI zip already exists and verified for ${ARCH}.")
    set(SHOULD_DOWNLOAD FALSE)
  else()
    message(WARNING "Existing NewPipeCLI zip checksum mismatch for ${ARCH}, re-downloading...")
  endif()
endif()

# Download if missing or invalid
if(SHOULD_DOWNLOAD)
  message(STATUS "Downloading NewPipeCLI for ${ARCH}...")
  file(DOWNLOAD ${NEWPIPECLI_URL} ${NEWPIPECLI_ZIP} SHOW_PROGRESS)

  # Verify checksum after download
  file(SHA256 ${NEWPIPECLI_ZIP} DOWNLOADED_SHA256)
  if(NOT DOWNLOADED_SHA256 STREQUAL NEWPIPECLI_SHA256)
    message(FATAL_ERROR "Downloaded NewPipeCLI zip checksum mismatch for ${ARCH}! Aborting build.")
  endif()
endif()

# List of absolute paths to libraries that should be bundled with the plugin.
set(flutter_new_pipe_extractor_bundled_libraries
  ""
  PARENT_SCOPE
)
