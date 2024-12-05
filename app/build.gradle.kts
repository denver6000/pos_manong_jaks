plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.denproj.posmanongjaks"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.denproj.posmanongjaks"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14" // or the latest compatible version
    }


}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.datastore.preferences)
    implementation(libs.core.ktx)
    val room_version = "2.6.1"

    implementation(libs.google.accompanist.swiperefresh)

    implementation(libs.androidx.core.ktx.v1120)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation(libs.androidx.activity.compose.v182)
    implementation(platform("androidx.compose:compose-bom:2024.11.00"))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.compose.material3.material3)


    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material3)

    implementation("co.yml:ycharts:2.1.0")

    implementation(libs.hilt.android.v2511)
    kapt(libs.hilt.android.compiler.v2511)

    val composeBom = platform("androidx.compose:compose-bom:2024.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)

    // Optional - Integration with activities
    implementation(libs.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.runtime.livedata)
    // Optional - Integration with RxJava
    implementation(libs.runtime.rxjava2)


    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    implementation("com.github.danielfelgar:draw-receipt:0.1.3")
    implementation("com.github.DantSu:ESCPOS-ThermalPrinter-Android:3.3.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.room.runtime)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    annotationProcessor(libs.room.compiler)
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.android.compiler)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

kapt {
    correctErrorTypes = true
}