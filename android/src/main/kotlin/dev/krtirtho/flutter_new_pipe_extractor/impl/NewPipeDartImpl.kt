package dev.krtirtho.flutter_new_pipe_extractor.impl

import dev.krtirtho.flutter_new_pipe_extractor.converter.Convert
import dev.krtirtho.flutter_new_pipe_extractor.downloader.DownloaderImpl
import dev.krtirtho.flutter_new_pipe_extractor.pigeon.JsonMessageMap
import dev.krtirtho.flutter_new_pipe_extractor.pigeon.NewPipeExtractor
import io.flutter.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.schabi.newpipe.extractor.NewPipe
import org.schabi.newpipe.extractor.search.SearchInfo
import org.schabi.newpipe.extractor.services.youtube.YoutubeService
import org.schabi.newpipe.extractor.stream.StreamInfo

class NewPipeDartImpl : NewPipeExtractor {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun getService(): YoutubeService {
        return NewPipe.getService(0) as YoutubeService
    }

    override fun init() {
        NewPipe.init(DownloaderImpl.init((OkHttpClient())))
    }


    override fun getVideoInfo(
        videoId: String,
        callback: (Result<JsonMessageMap>) -> Unit
    ) {
        scope.launch {
            try {
                var service = getService()

                var streamInfo = StreamInfo.getInfo(
                    service.getStreamExtractor(
                        service.streamLHFactory.fromId(videoId)
                    )
                )

                var jsonElement = Convert.streamInfoMap(streamInfo)

                withContext(Dispatchers.Main) {
                    callback(Result.success(JsonMessageMap(data = jsonElement)))
                }
            } catch (e: Exception) {
                Log.e("NewPipeDartImpl", "getVideoInfo: $e")
                withContext(Dispatchers.Main) {
                    callback(Result.failure(e))
                }
            }
        }
    }

    override fun search(
        query: String,
        contentFilters: List<String>?,
        sortFilter: String?,
        callback: (Result<List<JsonMessageMap>>) -> Unit
    ) {
        scope.launch {
            try {
                var service = getService()
                var searchResults = SearchInfo.getInfo(
                    service,
                    service.searchQHFactory.fromQuery(
                        query,
                        contentFilters ?: listOf(),
                        sortFilter ?: ""
                    )
                )

                var jsonElement = searchResults.relatedItems
                    .map {
                        JsonMessageMap(Convert.infoItemMap(it))
                    }

                withContext(Dispatchers.Main) {
                    callback(Result.success(jsonElement))
                }
            } catch (e: Exception) {
                Log.e("NewPipeDartImpl", "search: $e")
                withContext(Dispatchers.Main) {
                    callback(Result.failure(e))
                }
            }
        }
    }
}