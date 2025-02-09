// Autogenerated from Pigeon (v24.1.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon
@file:Suppress("UNCHECKED_CAST", "ArrayInDataClass")

package dev.krtirtho.flutter_new_pipe_extractor.pigeon

import android.util.Log
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MessageCodec
import io.flutter.plugin.common.StandardMethodCodec
import io.flutter.plugin.common.StandardMessageCodec
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

private fun wrapResult(result: Any?): List<Any?> {
  return listOf(result)
}

private fun wrapError(exception: Throwable): List<Any?> {
  return if (exception is FlutterError) {
    listOf(
      exception.code,
      exception.message,
      exception.details
    )
  } else {
    listOf(
      exception.javaClass.simpleName,
      exception.toString(),
      "Cause: " + exception.cause + ", Stacktrace: " + Log.getStackTraceString(exception)
    )
  }
}

/**
 * Error class for passing custom error details to Flutter via a thrown PlatformException.
 * @property code The error code.
 * @property message The error message.
 * @property details The error details. Must be a datatype supported by the api codec.
 */
class FlutterError (
  val code: String,
  override val message: String? = null,
  val details: Any? = null
) : Throwable()

/** Generated class from Pigeon that represents data sent in messages. */
data class JsonMessageMap (
  val data: Map<String, Any?>
)
 {
  companion object {
    fun fromList(pigeonVar_list: List<Any?>): JsonMessageMap {
      val data = pigeonVar_list[0] as Map<String, Any?>
      return JsonMessageMap(data)
    }
  }
  fun toList(): List<Any?> {
    return listOf(
      data,
    )
  }
}
private open class NewPipeApiPigeonCodec : StandardMessageCodec() {
  override fun readValueOfType(type: Byte, buffer: ByteBuffer): Any? {
    return when (type) {
      129.toByte() -> {
        return (readValue(buffer) as? List<Any?>)?.let {
          JsonMessageMap.fromList(it)
        }
      }
      else -> super.readValueOfType(type, buffer)
    }
  }
  override fun writeValue(stream: ByteArrayOutputStream, value: Any?)   {
    when (value) {
      is JsonMessageMap -> {
        stream.write(129)
        writeValue(stream, value.toList())
      }
      else -> super.writeValue(stream, value)
    }
  }
}


/** Generated interface from Pigeon that represents a handler of messages from Flutter. */
interface NewPipeExtractor {
  fun init()
  fun getVideoInfo(videoId: String, callback: (Result<JsonMessageMap>) -> Unit)
  fun search(query: String, contentFilters: List<String>?, sortFilter: String?, callback: (Result<List<JsonMessageMap>>) -> Unit)

  companion object {
    /** The codec used by NewPipeExtractor. */
    val codec: MessageCodec<Any?> by lazy {
      NewPipeApiPigeonCodec()
    }
    /** Sets up an instance of `NewPipeExtractor` to handle messages through the `binaryMessenger`. */
    @JvmOverloads
    fun setUp(binaryMessenger: BinaryMessenger, api: NewPipeExtractor?, messageChannelSuffix: String = "") {
      val separatedMessageChannelSuffix = if (messageChannelSuffix.isNotEmpty()) ".$messageChannelSuffix" else ""
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_new_pipe_extractor.NewPipeExtractor.init$separatedMessageChannelSuffix", codec)
        if (api != null) {
          channel.setMessageHandler { _, reply ->
            val wrapped: List<Any?> = try {
              api.init()
              listOf(null)
            } catch (exception: Throwable) {
              wrapError(exception)
            }
            reply.reply(wrapped)
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_new_pipe_extractor.NewPipeExtractor.getVideoInfo$separatedMessageChannelSuffix", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val videoIdArg = args[0] as String
            api.getVideoInfo(videoIdArg) { result: Result<JsonMessageMap> ->
              val error = result.exceptionOrNull()
              if (error != null) {
                reply.reply(wrapError(error))
              } else {
                val data = result.getOrNull()
                reply.reply(wrapResult(data))
              }
            }
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
      run {
        val channel = BasicMessageChannel<Any?>(binaryMessenger, "dev.flutter.pigeon.flutter_new_pipe_extractor.NewPipeExtractor.search$separatedMessageChannelSuffix", codec)
        if (api != null) {
          channel.setMessageHandler { message, reply ->
            val args = message as List<Any?>
            val queryArg = args[0] as String
            val contentFiltersArg = args[1] as List<String>?
            val sortFilterArg = args[2] as String?
            api.search(queryArg, contentFiltersArg, sortFilterArg) { result: Result<List<JsonMessageMap>> ->
              val error = result.exceptionOrNull()
              if (error != null) {
                reply.reply(wrapError(error))
              } else {
                val data = result.getOrNull()
                reply.reply(wrapResult(data))
              }
            }
          }
        } else {
          channel.setMessageHandler(null)
        }
      }
    }
  }
}
