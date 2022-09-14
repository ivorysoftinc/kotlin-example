plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ivorysoftinc.kotlin.example"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes.all {
        isMinifyEnabled = false
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.2.0"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core
    implementation("androidx.core:core-ktx:1.9.0")

    // Material
    implementation("com.google.android.material:material:1.6.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    // Compose
    implementation("androidx.compose.ui:ui:1.2.1")
    implementation("androidx.compose.material:material:1.2.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.2.1")
    implementation("androidx.activity:activity-compose:1.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling:1.2.1")

    // Networking
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.9.1"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.9.1")

    // Room
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    // Koin DI
    implementation("io.insert-koin:koin-android:3.2.1")

    // Glide
    implementation("com.github.skydoves:landscapist-glide:1.3.6")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.2.1")
    androidTestImplementation("io.insert-koin:koin-androidx-compose:3.1.2")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.2.1")
}
