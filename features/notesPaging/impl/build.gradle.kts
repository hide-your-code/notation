plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.features.notesPaging.impl"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
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

    implementation(project(":features:notesPaging:api"))
    implementation(project(":features:modifyNote:api"))

    implementation(libs.bundles.androidx.base)
    implementation(libs.bundles.kotlin.immutable)

    implementation(libs.bundles.timber)

    implementation(libs.kotlin.serialization)
    implementation(libs.bundles.lifecycle)
    implementation(libs.bundles.navigation3)
    implementation(libs.bundles.hilt.compose)

    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.bundles.paging.compose)

    
    androidTestImplementation(libs.bundles.androidx.test)
}
