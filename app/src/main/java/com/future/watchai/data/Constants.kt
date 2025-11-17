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
        "llama3-8b-8192",
        "llama3-70b-8192",
        "mixtral-8x7b-32768"
    )
}
