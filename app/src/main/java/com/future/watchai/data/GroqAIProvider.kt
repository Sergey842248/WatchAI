package com.future.watchai.data

import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class GroqAIProvider(
    private val apiKey: String,
    private val modelName: String
) : AIProvider {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    data class GroqRequest(
        val model: String,
        val messages: List<Map<String, String>>,
        val max_tokens: Int = 1024
    )

    data class GroqResponse(
        val choices: List<Choice>
    )

    data class Choice(
        val message: Message
    )

    data class Message(
        val content: String
    )

    override suspend fun generateContent(prompt: String): String {
        if (apiKey.isBlank()) {
            return "Error: Groq API key not provided"
        }

        return try {
            val requestBody = GroqRequest(
                model = modelName,
                messages = listOf(mapOf("role" to "user", "content" to prompt))
            )

            val jsonBody = gson.toJson(requestBody).toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("https://api.groq.com/openai/v1/chat/completions")
                .post(jsonBody)
                .addHeader("Authorization", "Bearer $apiKey")
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                val groqResponse = gson.fromJson(responseBody, GroqResponse::class.java)
                groqResponse.choices.firstOrNull()?.message?.content ?: "No response generated"
            } else {
                "Error: ${response.message} (Code: ${response.code})"
            }
        } catch (e: IOException) {
            "Error: Network request failed - ${e.message}"
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }
}
