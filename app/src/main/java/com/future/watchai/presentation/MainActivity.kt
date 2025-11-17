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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TextField
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
    var showGroqSettings by remember { mutableStateOf(false) }
    var groqApiKey by remember { mutableStateOf(prefsManager.getGroqApiKey()) }
    val scope = rememberCoroutineScope()

    val availableModels = remember(selectedProvider) {
        when (selectedProvider) {
            AIProviderType.GOOGLE -> Constants.GOOGLE_MODELS
            AIProviderType.GROQ -> Constants.GROQ_MODELS
        }
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(response) {
        if (response.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue.toInt())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 28.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeText(modifier = Modifier.padding(vertical = 16.dp))

        // Provider Selection
        CompactChip(
            onClick = { showProviderPicker = !showProviderPicker },
            label = { Text("Provider: ${selectedProvider.name}") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showProviderPicker) {
            Text("Select Provider")
            val providerPickerState = rememberPickerState(
                initialNumberOfOptions = AIProviderType.values().size,
                initiallySelectedOption = selectedProvider.ordinal
            )
            Picker(
                state = providerPickerState,
                modifier = Modifier.size(100.dp)
            ) {
                Text(AIProviderType.values()[it].name)
            }
            Button(
                onClick = {
                    val newProvider = AIProviderType.values()[providerPickerState.selectedOption]
                    selectedProvider = newProvider
                    prefsManager.setSelectedProvider(newProvider)
                    selectedModel = when (newProvider) {
                        AIProviderType.GOOGLE -> prefsManager.getSelectedGoogleModel()
                        AIProviderType.GROQ -> prefsManager.getSelectedGroqModel()
                    }
                    aiProvider = prefsManager.getCurrentApiProvider(context)
                    showProviderPicker = false
                }
            ) {
                Text("Set")
            }
        }

        // Model Selection
        CompactChip(
            onClick = { showModelPicker = !showModelPicker },
            label = { Text("Model: $selectedModel") },
            modifier = Modifier.fillMaxWidth()
        )

        if (showModelPicker) {
            Text("Select Model")
            val modelPickerState = rememberPickerState(
                initialNumberOfOptions = availableModels.size,
                initiallySelectedOption = availableModels.indexOf(selectedModel).coerceAtLeast(0)
            )
            Picker(
                state = modelPickerState,
                modifier = Modifier.size(100.dp)
            ) {
                Text(availableModels[it])
            }
            Button(
                onClick = {
                    selectedModel = availableModels[modelPickerState.selectedOption]
                    when (selectedProvider) {
                        AIProviderType.GOOGLE -> prefsManager.setSelectedGoogleModel(selectedModel)
                        AIProviderType.GROQ -> prefsManager.setSelectedGroqModel(selectedModel)
                    }
                    aiProvider = prefsManager.getCurrentApiProvider(context)
                    showModelPicker = false
                }
            ) {
                Text("Set")
            }
        }

        // Groq Settings (only for Groq provider)
        if (selectedProvider == AIProviderType.GROQ) {
            CompactChip(
                onClick = { showGroqSettings = !showGroqSettings },
                label = { Text("Groq API Key") },
                modifier = Modifier.fillMaxWidth()
            )

            if (showGroqSettings) {
                TextField(
                    value = groqApiKey,
                    onValueChange = { groqApiKey = it },
                    placeholder = { Text("Enter Groq API Key") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        prefsManager.setGroqApiKey(groqApiKey)
                        aiProvider = prefsManager.getCurrentApiProvider(context)
                        showGroqSettings = false
                    }
                ) {
                    Text("Save Key")
                }
            }
        }

        TextField(
            value = input,
            onValueChange = { input = it },
            placeholder = { Text("Enter your command") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

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
