Pod::Spec.new do |s|
  s.name             = 'flutter_new_pipe_extractor'
  s.version          = '0.0.1'
  s.summary          = 'A new Flutter plugin project.'
  s.description      = <<-DESC
A new Flutter plugin project.
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }

  s.source           = { :path => '.' }
  s.source_files     = 'Classes/**/*'

  s.dependency 'FlutterMacOS'

  s.platform = :osx, '10.11'
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }
  s.swift_version = '5.0'

  # --- Added section ---
  s.prepare_command = <<-CMD
    set -e
    ASSETS_DIR="../assets"
    ZIP_URL="https://github.com/KRTirtho/NewPipeCLI/releases/download/v0.1.0/NewPipeCLI-macos-universal.zip"
    ZIP_PATH="$ASSETS_DIR/NewPipeCLI-macos-universal.zip"

    mkdir -p "$ASSETS_DIR"

    if [ ! -f "$ZIP_PATH" ]; then
      echo "Downloading NewPipeCLI for macOS..."
      curl -L "$ZIP_URL" -o "$ZIP_PATH"
    fi
  CMD
  # --- End of added section ---
end
