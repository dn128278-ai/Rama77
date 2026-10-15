# Rama77 - Android Studio Kotlin Project (Package: com.rama77.app)

Clean, production-grade Android Studio project for **Rama77** with Material 3, Airbnb Lottie animations, Bumptech Glide image loading, Retrofit 2 networking, and Coroutines.

## 📦 APK Specifications:
- **Package Name**: `com.rama77.app`
- **Target SDK**: `34` (Android 14)
- **Minimum SDK**: `24` (Android 7.0)
- **Estimated APK Size**: ~15 MB – 20 MB (Includes universal 32/64-bit ARM & x86 native libraries, high-resolution vector assets, and raw Lottie JSON animations)
- **Architecture**: MVVM with Kotlin Coroutines + StateFlow / LiveData
- **Network Stack**: Retrofit 2 + OkHttp 4 + Gson Converter targeting `https://api.rama77.com/v1`

## 📂 Project Structure:
- `gradlew`, `gradlew.bat`: Gradle Wrapper command line scripts (Linux / macOS / Windows).
- `gradle/wrapper/gradle-wrapper.properties`: Gradle 8.7 distribution configuration.
- `gradle/wrapper/gradle-wrapper.jar`: Gradle Wrapper launcher binary.
- `settings.gradle`: Plugin management, repository configuration (Google, MavenCentral).
- `build.gradle`: Top-level root Android & Kotlin Gradle plugins.
- `gradle.properties`: AndroidX & Jetifier enabled, JVM memory options.
- `gradle/libs.versions.toml`: Version catalog managing Lottie, Glide, Material, Retrofit, Coroutines.
- `app/build.gradle`: compileSdk 34, viewBinding, ABI packaging, ProGuard rules.
- `app/proguard-rules.pro`: Preserves Lottie, Glide, Retrofit, Gson models for 15-20 MB release APK.
- `app/src/main/AndroidManifest.xml`: Network security config, INTERNET permissions, all Activities.
- `app/src/main/res/raw/`: Lottie JSON animations (`lottie_splash.json`, `lottie_wallet.json`, etc.).
- `app/src/main/res/drawable/`: High-resolution vector drawables & background gradients.
- `.github/workflows/build-apk.yml`: 1-click CI/CD pipeline to compile APK automatically on push!

## 🚀 Build Instructions:
1. Push this directory to your GitHub repository.
2. In GitHub, go to **Actions** -> **Build Rama77 APK** to build your APK automatically, or:
3. Open in **Android Studio** (Java 17 required).
4. Run `./gradlew assembleDebug` or `./gradlew assembleRelease`.
