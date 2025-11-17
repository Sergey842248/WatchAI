package com.future.watchai.data

import com.google.ai.client.generativeai.GenerativeModel

class GoogleAIProvider(
    private val apiKey: String,
    private val modelName: String
) : AIProvider {

    private val generativeModel = GenerativeModel(
        modelName = modelName,
        apiKey = apiKey
    )

    override suspend fun generateContent(prompt: String): String {
        return try {
            val result = generativeModel.generateContent(prompt)
            result.text ?: "No response generated"
        } catch (e: Exception) {
            "Error: ${e.message ?: "Unknown error"}"
        }
    }
}
