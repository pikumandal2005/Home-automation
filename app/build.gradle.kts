// File: build.gradle.kts (Module: app) - Final attempt to resolve library paths

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

// Define common versions used in your dependencies
val appCompatVersion = "1.6.1"
val coreKtxVersion = "1.12.0"
val lifecycleVersion = "2.6.2"

android {
    namespace = "com.example.smarthome"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.smarthome"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "3.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // Standard Android & Kotlin Core Libraries (Using direct strings now)
    implementation("androidx.core:core-ktx:$coreKtxVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    // Core AppCompat Library (Required for standard Android activities)
    implementation("androidx.appcompat:appcompat:$appCompatVersion")

    // CORE FIX: ConstraintLayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Biometric authentication library
    implementation("androidx.biometric:biometric:1.1.0")

    // Firebase Cloud Messaging for push notifications
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
    
    // WorkManager for background tasks
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Testing Dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}