import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.notation"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.notation"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(project(":features:welcome:api"))
    implementation(project(":features:welcome:impl"))

    implementation(project(":features:notesPaging:api"))
    implementation(project(":features:notesPaging:impl"))

    implementation(project(":features:notesManual:api"))
    implementation(project(":features:notesManual:impl"))

    implementation(project(":features:modifyNote:api"))
    implementation(project(":features:modifyNote:impl"))

    implementation(libs.bundles.androidx.base)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation3)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.timber)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.kotlin.serialization)

    ksp(libs.hilt.compiler)

    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.compose.android.test)
    debugImplementation(libs.bundles.compose.debug)
}