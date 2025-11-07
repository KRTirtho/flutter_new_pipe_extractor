import 'dart:io';

import 'package:flutter_new_pipe_extractor/src/desktop/api.dart';
import 'package:flutter_new_pipe_extractor/src/models/models.dart';
import 'package:flutter_new_pipe_extractor/src/pigeon/newpipe_api.g.dart'
    as api;
import 'package:flutter_new_pipe_extractor/src/utils/utils.dart';

export 'src/models/models.dart';

abstract class NewPipeExtractor {
  static final _hostApi =
      Platform.isAndroid ? api.NewPipeExtractor() : NewPipeDesktopApiImpl();

  static Future<void> init() {
    return _hostApi.init();
  }

  static Future<VideoInfo> getVideoInfo(String videoId) async {
    final json = await _hostApi.getVideoInfo(videoId);

    return VideoInfo.fromJson(deepCastMap(json.data));
  }

  static Future<List<SearchResultItem>> search(
    String query, {
    List<SearchContentFilters>? contentFilters,
    String? sortFilter,
  }) async {
    final json = await _hostApi.search(
      query,
      contentFilters: contentFilters?.map((e) => e.label).toList(),
      sortFilter: sortFilter,
    );

    return json
        .map((e) => switch (e.data["infoType"]) {
              "STREAM" => VideoSearchResultItem.fromJson(deepCastMap(e.data)),
              "PLAYLIST" =>
                PlaylistSearchResultItem.fromJson(deepCastMap(e.data)),
              "CHANNEL" =>
                ChannelSearchResultItem.fromJson(deepCastMap(e.data)),
              _ => throw Exception("Unknown type: ${e.data["type"]}"),
            })
        .toList();
  }
}
