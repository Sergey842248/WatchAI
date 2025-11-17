# WatchAI - Wear OS AI Assistant

A Wear OS app that uses AI models from Google Gemini and Groq directly on your smartwatch. Get AI responses right on your wrist!

## ✨ Features

- **Multiple AI Providers**: Choose between Google Gemini and Groq
- **Various Models**: Use different AI models depending on the provider
- **Wear OS Optimized**: Fully optimized user interface for Wear OS
- **API Key Management**: Secure storage of your API keys
- **Network Dependent**: Works with internet connection

## 🚀 Quick Start

### 1. Set up API Keys

Before you can use the app, you need to set up API keys for the desired providers.

#### Google Gemini API Key
1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Create a new API key
3. Add it to the `apikeys.properties` file (see below)

#### Groq API Key
1. Go to [Groq Console](https://console.groq.com/)
2. Create an account and API key
3. Add it to the `apikeys.properties` file

### 2. Configure API Keys

Create the file `app/src/main/assets/apikeys.properties`:

```properties
# API Keys for WatchAI
Google=YOUR_GOOGLE_API_KEY_HERE
Groq=YOUR_GROQ_API_KEY_HERE
```

**⚠️ Important:** This file is in `.gitignore` and will not be committed. Never share it in a public repository or space!

### 3. Build and Install App

```bash
# Clone project (if not already done)
git clone https://github.com/Sergey842248/WatchAI
cd WatchAI

# Load dependencies and build
./gradlew build

# Install on device/emulator
./gradlew installDebug
```

## 📱 Usage

### Getting Started
1. Open the WatchAI app on your Wear OS watch
2. Select your preferred AI provider (Google or Groq)
3. Select a model from the available list


## 🔧 Available Providers & Models

### Google Gemini
- **gemini-2.5-flash**: Fast and efficient
- **gemini-2.5-pro**: More advanced model

### Groq
- **openai/gpt-oss-120b**: High-performance model
- **meta-llama/llama-4-scout-17b-16e-instruct**: Open-Source model with good speed
- **qwen/qwen3-32b**: Thinking model

## 🔐 Security

- API keys are stored locally on the device
- The `apikeys.properties` file is excluded from Git
- Runtime API key input not possible through the app interface
- No data is sent to third parties (except to the respective AI providers)

## 🛠️ Development

### Prerequisites
- Android Studio Koala or later
- JDK 11
- Wear OS device or emulator

### Project Structure
```
app/src/main/java/com/future/watchai/
├── data/
│   ├── Constants.kt              # App constants
│   ├── PreferencesManager.kt     # Settings management
│   ├── GoogleAIProvider.kt       # Google Gemini integration
│   └── GroqAIProvider.kt         # Groq API integration
└── presentation/
    └── MainActivity.kt           # Main UI

app/src/main/assets/
└── apikeys.properties           # API keys (not committed)
```

### Adding Dependencies

If you want to develop new features, add dependencies in `gradle/libs.versions.toml`:


## 🔍 Troubleshooting

### "No response generated" Error
- Check your API keys in `apikeys.properties`
- Make sure your device has internet access

### Provider/Model not displayed
- Restart the app
- Check the provider/model selection

### Build Errors
```bash
# Clear cache
./gradlew clean

# Full rebuild
./gradlew clean build
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

- Open a Pull Request

## 🙋 Support

For questions or problems:
1. Check this README
2. Search the GitHub Issues
3. Open a new issue if necessary
