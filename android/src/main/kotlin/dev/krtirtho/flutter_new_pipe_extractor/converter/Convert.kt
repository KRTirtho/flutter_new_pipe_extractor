package dev.krtirtho.flutter_new_pipe_extractor.converter

import org.schabi.newpipe.extractor.Image
import org.schabi.newpipe.extractor.InfoItem
import org.schabi.newpipe.extractor.channel.ChannelInfoItem
import org.schabi.newpipe.extractor.localization.DateWrapper
import org.schabi.newpipe.extractor.playlist.PlaylistInfoItem
import org.schabi.newpipe.extractor.services.youtube.ItagItem
import org.schabi.newpipe.extractor.stream.AudioStream
import org.schabi.newpipe.extractor.stream.Description
import org.schabi.newpipe.extractor.stream.Stream
import org.schabi.newpipe.extractor.stream.StreamInfo
import org.schabi.newpipe.extractor.stream.StreamInfoItem
import org.schabi.newpipe.extractor.stream.VideoStream

class Convert {
    companion object {
        fun thumbnailMap(thumbnails: List<Image>): List<Map<String, Any?>> {
            return thumbnails.map {
                mapOf(
                    "url" to it.url,
                    "width" to it.width,
                    "height" to it.height
                )
            }
        }

        fun dateWrapperMap(date: DateWrapper): Map<String, Any?> {
            return mapOf(
                "offsetDateTime" to date.offsetDateTime().toEpochSecond(),
                "isApproximation" to date.isApproximation,
            )
        }

        fun descriptionMap(d: Description): Map<String, Any?> {
            return mapOf(
                "content" to d.content,
                "type" to d.type,
            )
        }

        fun itagItemMap(v: ItagItem): Map<String, Any?> {
            return mapOf(
                "mediaFormat" to v.mediaFormat.name,
                "id" to v.id,
                "itagType" to v.itagType.name,
                "avgBitrate" to v.avgBitrate,
                "sampleRate" to v.sampleRate,
                "audioChannels" to v.audioChannels,
                "resolutionString" to v.resolutionString,
                "fps" to v.fps,
                "bitrate" to v.bitrate,
                "width" to v.width,
                "height" to v.height,
                "initStart" to v.initStart,
                "initEnd" to v.initEnd,
                "indexStart" to v.indexStart,
                "indexEnd" to v.indexEnd,
                "quality" to v.quality,
                "codec" to v.codec,
                "targetDurationSec" to v.targetDurationSec,
                "approxDurationMs" to v.approxDurationMs,
                "contentLength" to v.contentLength,
                "audioTrackId" to v.audioTrackId,
                "audioTrackName" to v.audioTrackName,
                "audioTrackType" to v.audioTrackType,
//            "audioLocale" to v.audioLocale,
            )
        }

        fun streamMap(v: Stream): Map<String, Any?> {
            return mapOf(
                "id" to v.id,
                "mediaFormat" to v.format?.name,
                "content" to v.content,
                "isUrl" to v.isUrl,
                "deliveryMethod" to v.deliveryMethod.name,
                "manifestUrl" to v.manifestUrl,
            )
        }

        fun videoStreamMap(v: VideoStream): Map<String, Any?> {
            return streamMap(v) + mapOf(
                "resolution" to v.resolution,
                "isVideoOnly" to v.isVideoOnly,
                "itag" to v.itag,
                "bitrate" to v.bitrate,
                "initStart" to v.initStart,
                "initEnd" to v.initEnd,
                "indexStart" to v.indexStart,
                "indexEnd" to v.indexEnd,
                "width" to v.width,
                "height" to v.height,
                "fps" to v.fps,
                "quality" to v.quality,
                "codec" to v.codec,
                "itagItem" to if (v.itagItem != null) itagItemMap(v.itagItem as ItagItem) else null,
            )
        }

        fun audioStreamMap(v: AudioStream): Map<String, Any?> {
            return streamMap(v) + mapOf(
                "itag" to v.itag,
                "bitrate" to v.bitrate,
                "initStart" to v.initStart,
                "initEnd" to v.initEnd,
                "indexStart" to v.indexStart,
                "indexEnd" to v.indexEnd,
                "quality" to v.quality,
                "codec" to v.codec,
                "audioTrackId" to v.audioTrackId,
                "audioTrackName" to v.audioTrackName,
                "audioLocale" to v.audioLocale,
                "audioTrackType" to v.audioTrackType,
                "itagItem" to if (v.itagItem != null) itagItemMap(v.itagItem as ItagItem) else null,
            )
        }

        fun streamInfoMap(streamInfo: StreamInfo): Map<String, Any?> {
            val audioStreams = streamInfo.audioStreams.map(::audioStreamMap)
            val videoStreams = streamInfo.videoStreams.map(::videoStreamMap)

            return mapOf(
                "id" to streamInfo.id,
                "url" to streamInfo.url,
                "originalUrl" to streamInfo.originalUrl,
                "name" to streamInfo.name,
                "streamType" to streamInfo.streamType.name,
                "thumbnails" to thumbnailMap(streamInfo.thumbnails),
                "textualUploadDate" to streamInfo.textualUploadDate,
                "uploadDate" to dateWrapperMap(streamInfo.uploadDate),
                "duration" to streamInfo.duration,
                "ageLimit" to streamInfo.ageLimit,
                "description" to descriptionMap(streamInfo.description),
                "viewCount" to streamInfo.viewCount,
                "likeCount" to streamInfo.likeCount,
                "dislikeCount" to streamInfo.dislikeCount,
                "uploaderName" to streamInfo.uploaderName,
                "uploaderUrl" to streamInfo.uploaderUrl,
                "uploaderAvatars" to thumbnailMap(streamInfo.uploaderAvatars),
                "uploaderVerified" to streamInfo.isUploaderVerified,
                "uploaderSubscriberCount" to streamInfo.uploaderSubscriberCount,
                "subChannelName" to streamInfo.subChannelName,
                "subChannelUrl" to streamInfo.subChannelUrl,
                "subChannelAvatars" to streamInfo.subChannelAvatars,
                "videoStreams" to videoStreams,
                // Falling back to muxed streams if no separate audio streams are available
                "audioStreams" to audioStreams,
                "videoOnlyStreams" to streamInfo.videoOnlyStreams.map(::videoStreamMap),
                "dashMpdUrl" to streamInfo.dashMpdUrl,
                "hlsUrl" to streamInfo.hlsUrl,
//                    "relatedItems" to streamInfo.relatedItems,
                "startPosition" to streamInfo.startPosition,
//                    "subtitles" to streamInfo.subtitles,
                "host" to streamInfo.host,
//                    "privacy" to streamInfo.privacy,
                "category" to streamInfo.category,
                "licence" to streamInfo.licence,
                "supportInfo" to streamInfo.supportInfo,
//                    "language" to streamInfo.languageInfo,
                "tags" to streamInfo.tags,
//                    "streamSegments" to streamInfo.streamSegments,
//                    "metaInfo" to streamInfo.metaInfo,
                "shortFormContent" to streamInfo.isShortFormContent,
//                    "previewFrames" to streamInfo.previewFrames,
            )
        }

        private fun infoItemInternalMap(item: InfoItem): Map<String, Any?> {
            return mapOf(
                "infoType" to item.infoType.name,
//            "serviceId" to item.serviceId,
                "url" to item.url,
                "name" to item.name,
                "thumbnails" to thumbnailMap(item.thumbnails),
            )
        }

        fun playlistInfoItemMap(info: PlaylistInfoItem): Map<String, Any?> {
            return infoItemInternalMap(info) + mapOf(
                "uploaderName" to info.uploaderName,
                "uploaderUrl" to info.uploaderUrl,
                "uploaderVerified" to info.isUploaderVerified,
                "streamCount" to info.streamCount,
                "description" to descriptionMap(info.description),
                "playlistType" to info.playlistType.name,
            )
        }

        fun channelInfoItemMap(info: ChannelInfoItem): Map<String, Any?> {

            return infoItemInternalMap(info) + mapOf(
                "description" to info.description,
                "subscriberCount" to info.subscriberCount,
                "streamCount" to info.streamCount,
                "verified" to info.isVerified,
            )
        }

        fun streamInfoItemMap(info: StreamInfoItem): Map<String, Any?> {

            return infoItemInternalMap(info) + mapOf(
                "streamType" to info.streamType.name,
                "uploaderName" to info.uploaderName,
                "shortDescription" to info.shortDescription,
                "textualUploadDate" to info.textualUploadDate,
                "uploadDate" to if (info.uploadDate == null) null else dateWrapperMap(info.uploadDate as DateWrapper),
                "viewCount" to info.viewCount,
                "duration" to info.duration,
                "uploaderUrl" to info.uploaderUrl,
                "uploaderAvatars" to thumbnailMap(info.uploaderAvatars),
                "uploaderVerified" to info.isUploaderVerified,
                "shortFormContent" to info.isShortFormContent,
            )
        }


        fun infoItemMap(info: InfoItem): Map<String, Any?> {
            return when (info) {
                is PlaylistInfoItem -> playlistInfoItemMap(info)
                is StreamInfoItem -> streamInfoItemMap(info)
                is ChannelInfoItem -> channelInfoItemMap(info)
                else -> return infoItemInternalMap(info)
            }
        }
    }
}