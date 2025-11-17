package com.future.watchai.data

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("WatchAI_Prefs", Context.MODE_PRIVATE)

    // Load API keys from assets/apikeys.properties if available
    private val defaultApiKeys = loadDefaultApiKeys(context)

    companion object {
        private const val KEY_SELECTED_PROVIDER = "selected_provider"
        private const val KEY_GOOGLE_API_KEY = "google_api_key"
        private const val KEY_GROQ_API_KEY = "groq_api_key"
        private const val KEY_SELECTED_MODEL_GOOGLE = "selected_model_google"
        private const val KEY_SELECTED_MODEL_GROQ = "selected_model_groq"
    }

    private fun loadDefaultApiKeys(context: Context): Map<String, String> {
        return try {
            val properties = Properties()
            context.assets.open("apikeys.properties").use { inputStream ->
                properties.load(inputStream)
            }
            mapOf(
                "google" to (properties.getProperty("Google", "")),
                "groq" to (properties.getProperty("Groq", ""))
            )
        } catch (e: Exception) {
            // If file doesn't exist or can't be read, return empty defaults
            emptyMap()
        }
    }

    fun getSelectedProvider(): AIProviderType {
        val providerString = prefs.getString(KEY_SELECTED_PROVIDER, AIProviderType.GOOGLE.name)
        return try {
            AIProviderType.valueOf(providerString ?: AIProviderType.GOOGLE.name)
        } catch (e: IllegalArgumentException) {
            AIProviderType.GOOGLE
        }
    }

    fun setSelectedProvider(provider: AIProviderType) {
        prefs.edit().putString(KEY_SELECTED_PROVIDER, provider.name).apply()
    }

    fun getGoogleApiKey(): String {
        val overrideKey = prefs.getString(KEY_GOOGLE_API_KEY, null)
        return overrideKey ?: defaultApiKeys["google"] ?: ""
    }

    fun setGoogleApiKey(apiKey: String) {
        prefs.edit().putString(KEY_GOOGLE_API_KEY, apiKey).apply()
    }

    fun getGroqApiKey(): String {
        val overrideKey = prefs.getString(KEY_GROQ_API_KEY, null)
        return overrideKey ?: defaultApiKeys["groq"] ?: ""
    }

    fun setGroqApiKey(apiKey: String) {
        prefs.edit().putString(KEY_GROQ_API_KEY, apiKey).apply()
    }

    fun getSelectedGoogleModel(): String {
        return prefs.getString(KEY_SELECTED_MODEL_GOOGLE, Constants.GOOGLE_MODELS[0]) ?: Constants.GOOGLE_MODELS[0]
    }

    fun setSelectedGoogleModel(model: String) {
        prefs.edit().putString(KEY_SELECTED_MODEL_GOOGLE, model).apply()
    }

    fun getSelectedGroqModel(): String {
        return prefs.getString(KEY_SELECTED_MODEL_GROQ, Constants.GROQ_MODELS[0]) ?: Constants.GROQ_MODELS[0]
    }

    fun setSelectedGroqModel(model: String) {
        prefs.edit().putString(KEY_SELECTED_MODEL_GROQ, model).apply()
    }

    fun getCurrentApiProvider(context: Context): AIProvider {
        val providerType = getSelectedProvider()
        return when (providerType) {
            AIProviderType.GOOGLE -> {
                GoogleAIProvider(getGoogleApiKey(), getSelectedGoogleModel())
            }
            AIProviderType.GROQ -> {
                GroqAIProvider(getGroqApiKey(), getSelectedGroqModel())
            }
        }
    }
}
