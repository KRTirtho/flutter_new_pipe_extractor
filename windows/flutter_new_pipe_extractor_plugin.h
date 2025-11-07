#ifndef FLUTTER_PLUGIN_FLUTTER_NEW_PIPE_EXTRACTOR_PLUGIN_H_
#define FLUTTER_PLUGIN_FLUTTER_NEW_PIPE_EXTRACTOR_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace flutter_new_pipe_extractor {

class FlutterNewPipeExtractorPlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  FlutterNewPipeExtractorPlugin();

  virtual ~FlutterNewPipeExtractorPlugin();

  // Disallow copy and assign.
  FlutterNewPipeExtractorPlugin(const FlutterNewPipeExtractorPlugin&) = delete;
  FlutterNewPipeExtractorPlugin& operator=(const FlutterNewPipeExtractorPlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace flutter_new_pipe_extractor

#endif  // FLUTTER_PLUGIN_FLUTTER_NEW_PIPE_EXTRACTOR_PLUGIN_H_
