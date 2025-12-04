# ChorePal

A family chore management Android app that helps parents assign and track chores for their children, with a points-based reward system.

## Features

- **Parent & Child Accounts** - Parents can create accounts and invite children using a family code
- **Chore Management** - Create, assign, and track daily, weekly, and bonus chores
- **Photo Proof** - Children can submit photos as proof of completed chores
- **Points System** - Earn points for completing chores, redeem for rewards
- **Notifications** - Stay updated on chore assignments and approvals
- **Firebase Authentication** - Secure email-based authentication with verification

---

## Prerequisites

Before you begin, ensure you have the following installed:

### 1. Java Development Kit (JDK) 17
- Download from [Oracle JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK 17](https://adoptium.net/temurin/releases/?version=17)
- Set `JAVA_HOME` environment variable to your JDK installation path

### 2. Android Studio
- Download from [developer.android.com/studio](https://developer.android.com/studio)
- Recommended version: **Android Studio Hedgehog (2023.1.1)** or newer
- During installation, ensure you install:
  - Android SDK
  - Android SDK Platform-Tools
  - Android Emulator (for testing without a physical device)

### 3. Android SDK
- **Minimum SDK**: API 26 (Android 8.0 Oreo)
- **Target SDK**: API 35 (Android 15)
- Install via Android Studio: `Tools > SDK Manager > SDK Platforms`

---

## Installation

### Step 1: Clone the Repository

```bash
git clone https://github.com/Chinnabbaichowdary/Capstone.git
cd Capstone
```

### Step 2: Configure Android SDK Path

1. Copy the example properties file:
   ```bash
   cp local.properties.example local.properties
   ```

2. Open `local.properties` and set your Android SDK path:

   **Windows:**
   ```properties
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```

   **macOS:**
   ```properties
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```

   **Linux:**
   ```properties
   sdk.dir=/home/YourUsername/Android/Sdk
   ```

### Step 3: Firebase Configuration

The app uses Firebase for authentication. The `google-services.json` file is already included in the `app/` directory.

> **Note**: If you want to use your own Firebase project:
> 1. Go to [Firebase Console](https://console.firebase.google.com/)
> 2. Create a new project
> 3. Add an Android app with package name: `com.chorepal.app`
> 4. Download `google-services.json` and replace the existing one in `app/`
> 5. Enable **Email/Password** authentication in Firebase Console

### Step 4: Open in Android Studio

1. Launch Android Studio
2. Select **"Open"** and navigate to the cloned project folder
3. Wait for Gradle sync to complete (this may take a few minutes the first time)

### Step 5: Build the Project

**Option A: Via Android Studio**
- Click the **Build** menu → **Make Project** (or press `Ctrl+F9` / `Cmd+F9`)

**Option B: Via Command Line**
```bash
# Windows
gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

---

## Running the App

### Option 1: Android Emulator

1. Open **AVD Manager**: `Tools > Device Manager`
2. Create a new virtual device (recommended: Pixel 6 with API 33+)
3. Click the **Run** button or press `Shift+F10`

### Option 2: Physical Device

1. Enable **Developer Options** on your Android device:
   - Go to `Settings > About Phone`
   - Tap **Build Number** 7 times
2. Enable **USB Debugging**:
   - Go to `Settings > Developer Options`
   - Toggle on **USB Debugging**
3. Connect your device via USB
4. Click the **Run** button in Android Studio

---

## Usage Guide

### For Parents

1. **Sign Up** as a Parent
2. After email verification, you'll receive a **Family Code**
3. Share this code with your children
4. Create chores and assign them to children
5. Review and approve completed chores

### For Children

1. **Sign Up** as a Child
2. Enter the **Family Code** from your parent
3. View assigned chores on your dashboard
4. Complete chores and submit with photo proof
5. Earn points when chores are approved!

---

## Project Structure

```
ChorePal/
├── app/
│   ├── src/main/java/com/chorepal/app/
│   │   ├── auth/           # Authentication (AuthManager)
│   │   ├── data/           # Database, DAOs, Models, Repositories
│   │   ├── notifications/  # Push notifications
│   │   ├── ui/             # Compose UI screens & navigation
│   │   ├── utils/          # Utility classes
│   │   └── viewmodel/      # ViewModels
│   └── google-services.json
├── gradle/
├── build.gradle.kts
└── settings.gradle.kts
```

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Kotlin | Programming language |
| Jetpack Compose | Modern UI toolkit |
| Room Database | Local data persistence |
| Firebase Auth | User authentication |
| Firebase Firestore | Cloud database (optional sync) |
| Coroutines & Flow | Asynchronous programming |
| Material 3 | Design system |
| Coil | Image loading |
| DataStore | Preferences storage |

---

## Troubleshooting

### Gradle Sync Failed
- Ensure you have a stable internet connection
- Check that `local.properties` has the correct SDK path
- Try: `File > Invalidate Caches / Restart`

### Build Errors
- Update Android Studio to the latest version
- Run `./gradlew clean` then rebuild
- Check that JDK 17 is properly configured

### App Crashes on Launch
- Ensure `google-services.json` is in the `app/` folder
- Check Firebase project configuration
- Review Logcat for error details
