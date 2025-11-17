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

**⚠️ Important:** This file is in `.gitignore` and will not be committed. Never share it in the repository!

### 3. Build and Install App

```bash
# Clone project (if not already done)
git clone <repository-url>
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
4. For Groq: Enter your API key (if not already configured in the file)

### Using Chat
1. Tap on the text field
2. Enter your question/command
3. Tap "Send"
4. The AI response appears automatically

## 🔧 Available Providers & Models

### Google Gemini
- **gemini-2.5-flash**: Fast and efficient
- **gemini-2.5-pro**: More advanced model

### Groq
- **llama3-8b-8192**: Balanced performance
- **llama3-70b-8192**: High-performance model
- **mixtral-8x7b-32768**: Mixtral model with good speed

## 🔐 Security

- API keys are stored locally on the device
- The `apikeys.properties` file is excluded from Git
- Runtime API key input possible through the app interface
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

```toml
[versions]
my_lib = "1.0.0"

[libraries]
my_lib = { group = "com.example", name = "lib", version.ref = "my_lib" }
```

## 🔍 Troubleshooting

### "No response generated" Error
- Check your API keys in `apikeys.properties`
- Make sure your device has internet access
- For Groq: Verify that the key is active

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

1. Fork the project
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 🙋 Support

For questions or problems:
1. Check this README
2. Search the GitHub Issues
3. Open a new issue if necessary

## 📝 Changelog

### v1.0.0
- Initial release
- Support for Google Gemini and Groq
- Wear OS optimized UI
- Secure API key management
