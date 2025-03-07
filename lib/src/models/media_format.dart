part of 'models.dart';

enum MediaFormat {
  // video and audio combined formats
  //         id     name         suffix  mimeType
  @JsonValue("MPEG-4")
  mpeg4(id: 0x0, name: "MPEG-4", suffix: "mp4", mimeType: "video/mp4"),
  @JsonValue("3GPP")
  v3gpp(id: 0x10, name: "3GPP", suffix: "3gp", mimeType: "video/3gpp"),
  @JsonValue("WebM")
  webm(id: 0x20, name: "WebM", suffix: "webm", mimeType: "video/webm"),
  // audio formats
  @JsonValue("m4a")
  m4a(id: 0x100, name: "m4a", suffix: "m4a", mimeType: "audio/mp4"),
  @JsonValue("WebM")
  webma(id: 0x200, name: "WebM", suffix: "webm", mimeType: "audio/webm"),
  @JsonValue("MP3")
  mp3(id: 0x300, name: "MP3", suffix: "mp3", mimeType: "audio/mpeg"),
  @JsonValue("MP2")
  mp2(id: 0x310, name: "MP2", suffix: "mp2", mimeType: "audio/mpeg"),
  @JsonValue("opus")
  opus(id: 0x400, name: "opus", suffix: "opus", mimeType: "audio/opus"),
  @JsonValue("ogg")
  ogg(id: 0x500, name: "ogg", suffix: "ogg", mimeType: "audio/ogg"),
  @JsonValue("WebM Opus")
  webmaOpus(
      id: 0x200, name: "WebM Opus", suffix: "webm", mimeType: "audio/webm"),
  @JsonValue("AIFF")
  aiff(id: 0x600, name: "AIFF", suffix: "aiff", mimeType: "audio/aiff"),

  /// Same as [MediaFormat.aiff], just with the shorter suffix/file extension
  @JsonValue("AIFF")
  aif(id: 0x600, name: "AIFF", suffix: "aif", mimeType: "audio/aiff"),
  @JsonValue("WAV")
  wav(id: 0x700, name: "WAV", suffix: "wav", mimeType: "audio/wav"),
  @JsonValue("FLAC")
  flac(id: 0x800, name: "FLAC", suffix: "flac", mimeType: "audio/flac"),
  @JsonValue("ALAC")
  alac(id: 0x900, name: "ALAC", suffix: "alac", mimeType: "audio/alac"),
  // subtitles formats
  @JsonValue("WebVTT")
  vtt(id: 0x1000, name: "WebVTT", suffix: "vtt", mimeType: "text/vtt"),
  @JsonValue("Timed Text Markup Language")
  ttml(
      id: 0x2000,
      name: "Timed Text Markup Language",
      suffix: "ttml",
      mimeType: "application/ttml+xml"),
  @JsonValue("TranScript v1")
  transcript1(
      id: 0x3000, name: "TranScript v1", suffix: "srv1", mimeType: "text/xml"),
  @JsonValue("TranScript v2")
  transcript2(
      id: 0x4000, name: "TranScript v2", suffix: "srv2", mimeType: "text/xml"),
  @JsonValue("TranScript v3")
  transcript3(
      id: 0x5000, name: "TranScript v3", suffix: "srv3", mimeType: "text/xml"),
  @JsonValue("SubRip file format")
  srt(
      id: 0x6000,
      name: "SubRip file format",
      suffix: "srt",
      mimeType: "text/srt");

  final int id;
  final String name;
  final String suffix;
  final String mimeType;

  const MediaFormat({
    required this.id,
    required this.name,
    required this.suffix,
    required this.mimeType,
  });
}
