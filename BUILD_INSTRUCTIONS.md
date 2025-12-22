# Smart Home Android Build Instructions

## Prerequisites

- Android Studio (Latest version recommended)
- Android SDK 36
- JDK 21 (bundled with Android Studio)

## Build Configuration

The project has been configured to use Android Studio's bundled JDK to avoid Java version compatibility issues.

### Key Settings:
- **compileSdk**: 36
- **minSdk**: 24
- **targetSdk**: 36
- **Java Version**: 11
- **Kotlin Version**: 1.9.20
- **Android Gradle Plugin**: 8.7.3
- **App Version**: 3.0.0 (versionCode: 3)

## Building in Android Studio

1. **Open the Project**
   - Launch Android Studio
   - Click "Open" and select the `SmartHome` folder
   - Wait for Gradle sync to complete

2. **Verify JDK Settings**
   - Go to: File → Project Structure → SDK Location
   - Ensure "Gradle Settings" → "Gradle JDK" is set to Android Studio's bundled JDK
   - Path: `C:\Program Files\Android\Android Studio\jbr`

3. **Build APK**
   - Click: Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Or use: Build → Generate Signed Bundle / APK → APK
   - APK location: `app/build/outputs/apk/debug/app-debug.apk`

## Building from Command Line

1. **Clean Build**
   ```powershell
   .\gradlew clean
   ```

2. **Build Debug APK**
   ```powershell
   .\gradlew assembleDebug
   ```

3. **Build Release APK** (requires signing configuration)
   ```powershell
   .\gradlew assembleRelease
   ```

## Troubleshooting

### Java Version Issues
If you encounter "25.0.1" or similar Java version errors:
- The project is configured to use Android Studio's bundled JDK (Java 21)
- Verify `gradle.properties` contains:
  ```properties
  org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr
  ```

### Gradle Sync Fails
1. Stop all Gradle daemons:
   ```powershell
   .\gradlew --stop
   ```
2. Clear Gradle cache:
   ```powershell
   rm -r $env:USERPROFILE\.gradle\caches
   ```
3. Sync project again in Android Studio

### SDK Version Warnings
The warning about compileSdk 36 is suppressed in `gradle.properties`:
```properties
android.suppressUnsupportedCompileSdk=36
```

## APK Output

- **Debug APK**: `app/build/outputs/apk/debug/app-debug.apk`
- **Release APK**: `app/build/outputs/apk/release/app-release.apk`

## Features

- Fire Alarm System with real-time monitoring
- Sensor Data Dashboard (DHT11 temperature/humidity)
- PZEM Energy Monitoring
- Time-series charts with multiple time ranges (24H, 7D, 30D, 365D, ALL)
- Activity Logs with PDF export
- Firebase Integration (Authentication, Firestore, Storage, Realtime Database)
- Mobile-optimized UI with 44px touch targets

## Version History

### v3.0.0 (Current)
- Added 365D and ALL time range options
- Implemented log reversal and PDF export
- Fixed fire alarm auto-triggering on login
- Fixed Chart.js timestamp errors
- Optimized mobile UI for all screen sizes
- Added professional version badge

## Firebase Configuration

Ensure your `google-services.json` is placed in:
```
app/google-services.json
```

## Notes

- The app uses a hybrid WebView architecture
- Main UI is in: `app/src/main/assets/index.html`
- Separate web version available in: `index.html` (root directory)
- Both versions are kept in sync for consistent features
