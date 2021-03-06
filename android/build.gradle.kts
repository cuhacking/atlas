import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import io.gitlab.arturbosch.detekt.Detekt
import java.util.Properties
import java.io.FileInputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    id("com.android.application")
    kotlin("android")
    id("io.gitlab.arturbosch.detekt")
    id("com.cuhacking.mmapp")
    id("com.github.triplet.play") version "3.4.0-agp7.0"
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().takeIf { keystorePropertiesFile.exists() }?.apply {
    load(FileInputStream(keystorePropertiesFile))
}

android {
    compileSdk = libs.versions.compileSdk.getInt()
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.cuhacking.atlas"
        minSdk = libs.versions.minSdk.getInt()
        targetSdk = libs.versions.compileSdk.getInt()
        versionCode = generateVersionCode()
        versionName = generateVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (keystoreProperties != null) {
            create("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = rootDir.resolve(keystoreProperties.getProperty("storeFile"))
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        } else {
            println("No signing key, skipping signing config")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystoreProperties != null) {
                signingConfig = signingConfigs["release"]
            }
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
        useIR = true
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }
}

dependencies {
    implementation(projects.common)
    implementation(libs.bundles.androidx.runtime)
    implementation(libs.material)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.bundles.androidx.compose)

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

    withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}

play {
    // TODO: Set up production releases?
    track.set("internal")
    releaseStatus.set(ReleaseStatus.DRAFT)
}
