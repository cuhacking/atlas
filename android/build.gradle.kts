import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("com.android.application")
    kotlin("android")
    id("io.gitlab.arturbosch.detekt")
}

android {
    compileSdk = libs.versions.compileSdk.getInt()
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.cuhacking.atlas"
        minSdk = libs.versions.minSdk.getInt()
        targetSdk = libs.versions.compileSdk.getInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.common)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.material)
    implementation(libs.ktor.client.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.bundles.androidx.test)
    androidTestImplementation(libs.kotlinx.datetime)
    androidTestImplementation(libs.ktor.client.mock)
    androidTestImplementation(libs.ktor.client.json)
    androidTestImplementation(libs.ktor.client.serialization)
}

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/detekt.yml")
}

tasks {
    withType<Detekt> {
        jvmTarget = "11"
    }
}
