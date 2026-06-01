package dev.krtirtho.flutter_new_pipe_extractor.downloader

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import java.io.IOException

internal class DownloaderImpl private constructor(private val client: OkHttpClient) : Downloader() {

    private val cookies = HashMap<String, String>()

    fun getClient(): OkHttpClient {
        return client
    }

    fun getCookies(url: String): String {
        val youtubeCookie = if (url.contains(YOUTUBE_DOMAIN)) {
            getCookie(YOUTUBE_RESTRICTED_MODE_COOKIE_KEY)
        } else {
            null
        }

        val recaptchaCookie = getCookie("recaptcha_cookies")

        return listOfNotNull(youtubeCookie, recaptchaCookie)
            .flatMap { splitCookies(it) }
            .distinct()
            .joinToString("; ")
    }

    fun getCookie(key: String): String? {
        return cookies[key]
    }

    fun setCookie(key: String, cookie: String) {
        cookies[key] = cookie
    }

    fun removeCookie(key: String) {
        cookies.remove(key)
    }

    /**
     * Get the size of the content that the url is pointing by firing a HEAD request.
     *
     * @param url an url pointing to the content
     * @return the size of the content, in bytes
     */
    @Throws(IOException::class)
    fun getContentLength(url: String): Long {
        return try {
            val response = head(url)
            response.getHeader("Content-Length")?.toLong() ?: throw IOException("Missing content length")
        } catch (e: NumberFormatException) {
            throw IOException("Invalid content length", e)
        } catch (e: ReCaptchaException) {
            throw IOException(e)
        }
    }

    @Throws(IOException::class, ReCaptchaException::class)
    override fun execute(request: Request): Response {
        val httpMethod = request.httpMethod()
        val url = request.url()
        val headers = request.headers()
        val dataToSend = request.dataToSend()
        val requestBody: RequestBody? = dataToSend?.toRequestBody()

        val requestBuilder = okhttp3.Request.Builder()
            .method(httpMethod, requestBody)
            .url(url)
            .addHeader("User-Agent", USER_AGENT)

        val cookiesValue = getCookies(url)
        if (cookiesValue.isNotEmpty()) {
            requestBuilder.addHeader("Cookie", cookiesValue)
        }

        for ((headerName, headerValueList) in headers) {
            requestBuilder.removeHeader(headerName)
            for (headerValue in headerValueList) {
                requestBuilder.addHeader(headerName, headerValue)
            }
        }

        client.newCall(requestBuilder.build()).execute().use { response ->
            if (response.code == 429) {
                throw ReCaptchaException("reCaptcha Challenge requested", url)
            }

            val responseBodyToReturn = response.body?.string()
            val latestUrl = response.request.url.toString()

            return Response(
                response.code,
                response.message,
                response.headers.toMultimap(),
                responseBodyToReturn,
                latestUrl
            )
        }
    }

    private fun splitCookies(cookies: String): List<String> {
        return cookies.split("; *".toRegex())
    }

    companion object {

        const val USER_AGENT: String =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0"

        const val YOUTUBE_RESTRICTED_MODE_COOKIE_KEY: String = "youtube_restricted_mode_key"

        const val YOUTUBE_RESTRICTED_MODE_COOKIE: String = "PREF=f2=8000000"

        const val YOUTUBE_DOMAIN: String = "youtube.com"

        @JvmStatic
        var instance: DownloaderImpl? = null
            private set

        /**
         * It's recommended to call exactly once in the entire lifetime of the application.
         *
         * @param builder if null, default builder will be used
         * @return a new instance of [DownloaderImpl]
         */
        @JvmStatic
        fun init(client: OkHttpClient): DownloaderImpl {
            val newInstance = DownloaderImpl(client)
            instance = newInstance
            return newInstance
        }
    }
}