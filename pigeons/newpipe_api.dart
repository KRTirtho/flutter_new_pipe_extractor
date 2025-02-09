import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/pigeon/newpipe_api.g.dart',
  dartOptions: DartOptions(),
  kotlinOut:
      'android/src/main/kotlin/dev/krtirtho/flutter_new_pipe_extractor/pigeon/NewPipeApi.g.kt',
  kotlinOptions: KotlinOptions(
    package: 'dev.krtirtho.flutter_new_pipe_extractor.pigeon',
  ),
  // swiftOut: 'ios/Runner/pigeon/TextApi.g.swift',
  // swiftOptions: SwiftOptions(),
  dartPackageName: 'flutter_new_pipe_extractor',
))
@HostApi()
abstract class NewPipeExtractor {
  void init();
}
