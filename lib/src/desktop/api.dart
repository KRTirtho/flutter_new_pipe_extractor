import 'dart:convert';
import 'dart:io';

import 'package:archive/archive_io.dart';
import 'package:flutter/services.dart';
import 'package:flutter_new_pipe_extractor/src/pigeon/newpipe_api.g.dart';
import 'package:path/path.dart';
import 'package:path_provider/path_provider.dart';

const platformFileNames = {
  "linux-x64": "NewPipeCLI-linux-x64.zip",
  "linux-arm64": "NewPipeCLI-linux-arm64.zip",
  "windows-x64": "NewPipeCLI-windows-x64.zip",
  "macos": "NewPipeCLI-macos-universal.zip",
};

class NewPipeDesktopApiImpl implements NewPipeExtractor {
  File? executableFile;

  @override
  Future<void> init() async {
    assert(executableFile == null, "Already initialized");

    final appSupportDir = await getApplicationSupportDirectory();
    await appSupportDir.create(recursive: true);
    final binaryName = Platform.isWindows ? 'NewPipeCLI.exe' : 'NewPipeCLI';

    final destBinary = File(join(appSupportDir.path, binaryName));

    // if (await destBinary.exists()) {
    //   executableFile = destBinary;
    //   return;
    // }

    final arch = Platform.isMacOS || Platform.isIOS
        ? ""
        : Platform.version.contains(RegExp("x64|x86_64"))
            ? "-x64"
            : "-arm64";
    final zipName =
        platformFileNames["${Platform.operatingSystem}$arch"] as String;
    final bytes = await rootBundle
        .load('packages/flutter_new_pipe_extractor/assets/$zipName');
    final data = bytes.buffer.asUint8List();

    final tempDir = await getTemporaryDirectory();
    final zipPath = join(tempDir.path, zipName);
    final file = await File(zipPath).writeAsBytes(data);

    final archive = ZipDecoder().decodeBytes(await file.readAsBytes());
    for (final entry in archive) {
      final outPath = join(tempDir.path, entry.name);
      if (entry.isFile) {
        await File(outPath).create(recursive: true);
        await File(outPath).writeAsBytes(entry.content as List<int>);
      } else {
        await Directory(outPath).create(recursive: true);
      }
    }

    final srcBinary = File(join(tempDir.path, binaryName));

    await srcBinary.copy(destBinary.path);

    if (!Platform.isWindows) {
      await Process.run('chmod', ['+x', destBinary.path]);
    }

    executableFile = destBinary;
  }

  Future<String> executeString(List<String> args) async {
    if (executableFile == null) throw Exception("Not initialized");

    final result = await Process.run(
      executableFile!.path,
      args,
      // runInShell: true,
      // NewPipeCLI may create temp files, so we set the working directory to a writable temp dir
      workingDirectory: Directory.systemTemp.path,
    );

    final stdOut = result.stdout.toString();
    final stdErr = result.stderr.toString();

    if (stdErr.isNotEmpty && stdOut.isEmpty) {
      throw Exception(
        "NewPipeCLI command failed with exit code ${result.exitCode}\n"
        "STDOUT: $stdOut\n"
        "STDERR: $stdErr",
      );
    }

    return stdOut;
  }

  @override
  // ignore: non_constant_identifier_names
  BinaryMessenger? get pigeonVar_binaryMessenger => null;

  @override
  // ignore: non_constant_identifier_names
  String get pigeonVar_messageChannelSuffix => "";

  @override
  Future<JsonMessageMap> getVideoInfo(String videoId) async {
    final res = await executeString([
      "--streams",
      videoId,
    ]);

    return JsonMessageMap.decode([jsonDecode(res)]);
  }

  @override
  Future<List<JsonMessageMap>> search(
    String query, {
    List<String>? contentFilters,
    String? sortFilter,
  }) async {
    final res = await executeString([
      "--search",
      query,
      if (contentFilters != null) ...[
        "--content-filters",
        contentFilters.join(","),
      ],
      if (sortFilter != null) ...[
        "--sort-filter",
        sortFilter,
      ],
    ]);

    final decoded = jsonDecode(res) as List;

    return decoded.map((e) => JsonMessageMap.decode([e])).toList();
  }
}
