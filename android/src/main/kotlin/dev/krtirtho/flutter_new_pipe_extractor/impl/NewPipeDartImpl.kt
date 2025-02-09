package dev.krtirtho.flutter_new_pipe_extractor.impl

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dev.krtirtho.flutter_new_pipe_extractor.downloader.DownloaderImpl
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
import java.time.OffsetDateTime

class NewPipeDartImpl : NewPipeExtractor {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val gson = GsonBuilder()
        .registerTypeAdapter(
            OffsetDateTime::class.java,
            OffsetDateTimeAdapterEpoch()
        ).create()

    fun getService(): YoutubeService {
        return NewPipe.getService(0) as YoutubeService
    }

    override fun init() {
        NewPipe.init(DownloaderImpl((OkHttpClient())))
    }


    override fun getVideoInfo(
        videoId: String,
        callback: (Result<String>) -> Unit
    ) {
        scope.launch {
            try {
                var service = getService()
                var url = "https://www.youtube.com/watch?v=$videoId"

                var streamInfo = StreamInfo.getInfo(
                    service.getStreamExtractor(
                        service.streamLHFactory.fromId(videoId)
                    )
                )

                var jsonInfo = gson.toJson(streamInfo)

                withContext(Dispatchers.Main) {
                    callback(Result.success(jsonInfo))
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
        callback: (Result<String>) -> Unit
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

                var items = gson.toJson(searchResults.relatedItems)

                withContext(Dispatchers.Main) {
                    callback(Result.success(items))
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