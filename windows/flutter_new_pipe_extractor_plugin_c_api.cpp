#include "include/flutter_new_pipe_extractor/flutter_new_pipe_extractor_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "flutter_new_pipe_extractor_plugin.h"

void FlutterNewPipeExtractorPluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  flutter_new_pipe_extractor::FlutterNewPipeExtractorPlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
