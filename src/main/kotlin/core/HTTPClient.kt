package core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class HTTPClient {
    private val client = OkHttpClient()
    private val mapper = ObjectMapper()

    fun execute(url: String, args: Map<String, String>? = null): Map<String, Any>? {
        val urlBuilder = url.toHttpUrlOrNull()?.newBuilder()

        if (args != null) {
            for ((key, value) in args) {
                urlBuilder?.addQueryParameter(key, value)
            }
        }

        val finalUrl = urlBuilder?.build().toString()
        val request = Request.Builder().url(finalUrl!!).build()

        return try {
            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }

            val json = response.body?.string() ?: ""
            mapper.readValue<Map<String, Any>>(json, object : TypeReference<Map<String, Any>>() {})
        } catch (e: Exception) {
            null
        }
    }

    fun execute(url: String): Map<String, Any>? {
        return execute(url, null)
    }
}