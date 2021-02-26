plugins {
    id("com.android.application")
    kotlin("android")
    id(deps.plugins.detekt)
}

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "com.cuhacking.atlas"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.compileSdk)
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":common"))
    implementation(deps.androidx.core)
    implementation(deps.androidx.appCompat)
    implementation(deps.material)
    implementation(deps.androidx.constraintLayout)
    implementation(deps.androidx.lifeCycle)

    testImplementation(deps.junit)
    androidTestImplementation(deps.androidx.junit)
    androidTestImplementation(deps.androidx.espresso)
    androidTestImplementation(deps.kotlin.datetime)
    androidTestImplementation(deps.ktor.mockClient)
    androidTestImplementation(deps.ktor.jsonSerializer)
    androidTestImplementation(deps.ktor.jsonFeature)
}

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/detekt.yml")
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "1.8"
    }
}
