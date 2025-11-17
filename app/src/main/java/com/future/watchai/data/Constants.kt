package com.future.watchai.data

enum class AIProviderType {
    GOOGLE,
    GROQ
}

interface AIProvider {
    suspend fun generateContent(prompt: String): String
}

object Constants {
    // Models available per provider
    val GOOGLE_MODELS = listOf(
        "gemini-2.5-flash",
        "gemini-2.5-pro",
    )

    val GROQ_MODELS = listOf(
        "openai/gpt-oss-120b",
        "meta-llama/llama-4-scout-17b-16e-instruct",
        "qwen/qwen3-32b"
    )
}
