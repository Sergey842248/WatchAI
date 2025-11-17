/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.future.watchai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.*
import androidx.compose.material3.TextField
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.future.watchai.data.*
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp(context: android.content.Context = androidx.compose.ui.platform.LocalContext.current) {
    val prefsManager = remember { PreferencesManager(context) }

    var selectedProvider by remember { mutableStateOf(prefsManager.getSelectedProvider()) }
    var selectedModel by remember(selectedProvider) {
        mutableStateOf(
            when (selectedProvider) {
                AIProviderType.GOOGLE -> prefsManager.getSelectedGoogleModel()
                AIProviderType.GROQ -> prefsManager.getSelectedGroqModel()
            }
        )
    }

    var aiProvider by remember(selectedProvider, selectedModel) {
        mutableStateOf(prefsManager.getCurrentApiProvider(context))
    }

    var input by remember { mutableStateOf("") }
    var response by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showProviderPicker by remember { mutableStateOf(false) }
    var showModelPicker by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val availableModels = remember(selectedProvider) {
        when (selectedProvider) {
            AIProviderType.GOOGLE -> Constants.GOOGLE_MODELS
            AIProviderType.GROQ -> Constants.GROQ_MODELS
        }
    }

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 28.dp)
    ) {
        item { TimeText(modifier = Modifier.padding(vertical = 16.dp)) }

        item {
            Chip(
                onClick = { showProviderPicker = !showProviderPicker },
                label = { Text("Provider: ${selectedProvider.name}") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            if (showProviderPicker) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Provider")
                    AIProviderType.values().forEach { provider ->
                        CompactChip(
                            onClick = {
                                selectedProvider = provider
                                prefsManager.setSelectedProvider(provider)
                                selectedModel = when (provider) {
                                    AIProviderType.GOOGLE -> prefsManager.getSelectedGoogleModel()
                                    AIProviderType.GROQ -> prefsManager.getSelectedGroqModel()
                                }
                                aiProvider = prefsManager.getCurrentApiProvider(context)
                                showProviderPicker = false
                            },
                            label = { Text(provider.name) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        item {
            Chip(
                onClick = { showModelPicker = !showModelPicker },
                label = { Text("Model: $selectedModel") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            if (showModelPicker) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Model")
                    availableModels.forEach { model ->
                        CompactChip(
                            onClick = {
                                selectedModel = model
                                when (selectedProvider) {
                                    AIProviderType.GOOGLE -> prefsManager.setSelectedGoogleModel(model)
                                    AIProviderType.GROQ -> prefsManager.setSelectedGroqModel(model)
                                }
                                aiProvider = prefsManager.getCurrentApiProvider(context)
                                showModelPicker = false
                            },
                            label = { Text(model) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        item {
            TextField(
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Enter your command") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
        }

        item {
            Button(
                onClick = {
                    if (input.isNotBlank()) {
                        scope.launch {
                            isLoading = true
                            try {
                                response = aiProvider.generateContent(input)
                            } catch (exception: Exception) {
                                response = "Error: ${exception.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading && input.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoading) "Sending..." else "Send")
            }
        }

        item {
            if (response.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .heightIn(max = 200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = response,
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
