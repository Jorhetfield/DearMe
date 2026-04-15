# 💌 DearMe - Time Capsule App

> Send messages to your future self and unlock them when the time comes.

<div align="center">

[![Kotlin](https://img.shields.io/badge/Kotlin-2.1.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-512DA8?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Min API](https://img.shields.io/badge/Min%20API-26%20(Android%208.0)-3DDC84?logo=android&logoColor=white)](https://www.android.com/)
[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

</div>

---

## 🌟 Features

- ✉️ **Create Time Capsules** - Write letters and messages to your future self
- 🔐 **Secure Storage** - All capsules are encrypted and stored locally + Firebase backup
- 🔔 **Smart Notifications** - Get notified when a capsule is ready to open
- 🎨 **Beautiful UI** - Modern Material Design 3 with expressive animations
- 👤 **User Authentication** - Secure sign-up and login with Firebase
- 🎯 **Capsule Management** - View, edit, and organize your capsules
- 📱 **Responsive Design** - Optimized for all Android devices (Android 8.0+)

---

## 📱 Getting Started

### Prerequisites

- **Android Studio** (Latest version recommended)
- **Android SDK 36** or higher
- **Java 17** or higher
- **Gradle 8.x**

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Jorhetfield/DearMe.git
   cd DearMe
   ```

2. **Open in Android Studio**
   - Select "Open an existing project"
   - Navigate to the cloned directory
   - Let Gradle sync automatically

3. **Configure Firebase** (Optional for testing)
   - Create a Firebase project at [firebase.google.com](https://firebase.google.com)
   - Add your Android app to the project
   - Download `google-services.json` and place it in the `app/` directory
   - **For CI/CD:** See [SETUP_FIREBASE_SECRET.md](SETUP_FIREBASE_SECRET.md) to configure GitHub Actions with Firebase credentials (recommended for team collaboration)

4. **Build the project**
   ```bash
   # Using Gradle wrapper
   ./gradlew build

   # Or run on connected device/emulator
   ./gradlew installDebug
   ```

---

## 🏗️ Architecture

### Tech Stack
- **Language:** Kotlin 2.1.0
- **UI Framework:** Jetpack Compose with Material Design 3
- **Database:** Room (Local) + Firebase Firestore (Cloud)
- **Authentication:** Firebase Authentication
- **Notifications:** Firebase Cloud Messaging + WorkManager
- **Dependency Injection:** Hilt
- **Image Loading:** Coil
- **Navigation:** Jetpack Navigation Compose

### Project Structure
```
DearMe/
├── app/
│   └── src/main/java/es/jorhetfield/dearme/
│       ├── data/              # Data layer (Room, Preferences, Repositories)
│       ├── domain/            # Business logic & use cases
│       ├── firebase/          # Firebase integration
│       ├── ui/                # UI components & screens
│       │   ├── components/    # Reusable UI components
│       │   ├── screens/       # Feature screens
│       │   ├── theme/         # Material Design 3 theme
│       │   └── navigation/    # Navigation configuration
│       └── util/              # Utilities & helpers
└── gradle/                    # Gradle configurations
```

---

## 🚀 Download & Test

### Option 1: Direct APK Download (GitHub Releases)
The latest APK is available in the [Releases](../../releases) section.

1. Go to **Releases** tab
2. Download the latest `dearme-release.apk`
3. On your Android device:
   - Enable "Install from Unknown Sources" in Settings → Security
   - Transfer the APK file to your device
   - Tap to install

### Option 2: Build from Source
```bash
# Build release APK
./gradlew assembleRelease

# APK will be at: app/build/outputs/apk/release/app-release.apk
```

### Option 3: GitHub Actions Workflow (CI/CD)
The project has automatic builds configured:
- Commits to `main` trigger a test build
- Release branches trigger optimized APK builds
- Download artifacts directly from GitHub Actions

---

## 📋 Requirements

### Minimum
- **Android 8.0** (API 26) or higher
- **~50 MB** storage space

### Recommended
- **Android 13+** for best experience
- Stable internet connection for Cloud Sync
- Device with 4GB+ RAM

### Permissions
- `POST_NOTIFICATIONS` - For capsule unlock notifications
- `INTERNET` - For Firebase sync
- `ACCESS_NETWORK_STATE` - For connectivity checks

---

## 🔧 Development

### Build Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK (optimized)
./gradlew assembleRelease

# Run tests
./gradlew test

# Lint code
./gradlew lint

# Clean build
./gradlew clean build
```

### Debugging

```bash
# Install and run debug build
./gradlew installDebug

# View logs in real-time
adb logcat | grep dearme
```

---

## 🎯 Versioning

- **Current Version:** 1.0
- **Min SDK:** 26 (Android 8.0)
- **Target SDK:** 36 (Android 15)
- **Compile SDK:** 36

---

## 📝 Git Workflow

This project follows **Git Flow**:
- `main` - Production releases
- `develop` - Integration branch
- `feature/*` - Feature branches
- `release/*` - Release preparation
- `hotfix/*` - Emergency fixes

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit your changes: `git commit -m "Add feature description"`
3. Push to the branch: `git push origin feature/your-feature`
4. Open a Pull Request to `develop` branch

---

## 🐛 Bug Reports

Found a bug? Please open an [Issue](../../issues) with:
- Device model and Android version
- Steps to reproduce
- Expected vs actual behavior
- Screenshots/logs if applicable

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Jorge García Carboneras** ([@Jorhetfield](https://github.com/Jorhetfield))

---

## 🙏 Acknowledgments

- Material Design 3 team for the beautiful design system
- Jetpack Compose team for the modern UI framework
- Firebase team for the cloud infrastructure

---

<div align="center">

**Made with ❤️ in Madrid**

[GitHub](https://github.com/Jorhetfield) • [Portfolio](https://jorhetfield.dev)

</div>
