import org.apache.tools.ant.util.JavaEnvUtils.VERSION_1_8

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
//    alias(libs.plugins.compose.compiler)
    id ("kotlin-kapt")
    kotlin("plugin.serialization") version "1.9.20"
    alias(libs.plugins.googleGmsGoogleServices)

}

android {

    lint{
        abortOnError = false
    }
    namespace = "com.example.erp.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.erp.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.appcompat)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.compose)



    //coil
    implementation(libs.coil.compose)
    implementation (libs.androidx.material)
    //SplashScreen
    implementation(libs.androidx.core.splashscreen)
    //Constraint
    implementation(libs.androidx.constraintlayout.compose)

    //Icons
    implementation (libs.androidx.material.icons.core)
    implementation (libs.androidx.material.icons.extended)
    //kotlin Gson lib
    implementation (libs.gson)

    //flowLayout
    implementation (libs.foundation.v143)

    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    // Coil
    implementation (libs.coil.compose.v140)

    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.google.firebase:firebase-messaging")
    implementation("androidx.compose.material3:material3:1.3.0-beta04")


    implementation ("com.github.bumptech.glide:glide:4.14.2") // Use the latest version
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.airbnb.android:lottie-compose:5.0.3")

//    kapt ("com.github.bumptech.glide:compiler:4.14.2") // For annotation processing
}