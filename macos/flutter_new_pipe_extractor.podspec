Pod::Spec.new do |s|
  s.name             = 'flutter_new_pipe_extractor'
  s.version          = '0.0.1'
  s.summary          = 'A new Flutter plugin project.'
  s.description      = <<-DESC
A new Flutter plugin project.
                       DESC
  s.homepage         = 'http://github.com/KRTirtho/flutter_new_pipe_extractor'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }

  s.source           = { :path => '.' }
  s.source_files     = 'Classes/**/*'

  s.dependency 'FlutterMacOS'

  s.platform = :osx, '10.11'
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES' }
  s.swift_version = '5.0'

  # --- Prepare command to download and verify NewPipeCLI ---
  s.prepare_command = <<-CMD
    set -e
    ASSETS_DIR="../assets"
    ZIP_URL="https://github.com/KRTirtho/NewPipeCLI/releases/download/v0.1.0/NewPipeCLI-macos-universal.zip"
    ZIP_PATH="$ASSETS_DIR/NewPipeCLI-macos-universal.zip"

    # Expected SHA256 checksum of the release zip
    EXPECTED_SHA256="c0d97cc7f39967b58169e05fa87baba393f4a587210e446d88b99c06b5d02552"

    mkdir -p "$ASSETS_DIR"

    # Download if missing or checksum mismatch
    if [ -f "$ZIP_PATH" ]; then
      ACTUAL_SHA256=$(shasum -a 256 "$ZIP_PATH" | awk '{print $1}')
      if [ "$ACTUAL_SHA256" = "$EXPECTED_SHA256" ]; then
        echo "NewPipeCLI zip already exists and verified."
      else
        echo "Checksum mismatch, re-downloading NewPipeCLI..."
        rm "$ZIP_PATH"
      fi
    fi

    if [ ! -f "$ZIP_PATH" ]; then
      echo "Downloading NewPipeCLI for macOS..."
      curl -L "$ZIP_URL" -o "$ZIP_PATH"

      # Verify checksum after download
      ACTUAL_SHA256=$(shasum -a 256 "$ZIP_PATH" | awk '{print $1}')
      if [ "$ACTUAL_SHA256" != "$EXPECTED_SHA256" ]; then
        echo "Downloaded NewPipeCLI zip checksum mismatch! Aborting build."
        exit 1
      fi
    fi
  CMD
  # --- End of prepare command ---
end
