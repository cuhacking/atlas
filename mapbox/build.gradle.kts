plugins {
    id("com.android.library")
    kotlin("multiplatform")
}

android {
    compileSdkVersion(Versions.compileSdk)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
        getByName("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
            manifest.srcFile("src/androidTest/AndroidManifest.xml")
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    packagingOptions {
        exclude("META-INF/kotlinx-serialization-runtime.kotlin_module")
    }
}

kotlin {
    android()
    js {
        browser()
    }
    ios()

    sourceSets["commonMain"].dependencies {
        implementation(deps.kotlin.stdlibCommon)
    }

    sourceSets["commonTest"].dependencies {
        implementation(deps.kotlin.test.common)
        implementation(deps.kotlin.test.annotationsCommon)
    }

    sourceSets["androidMain"].dependencies {
        implementation(deps.kotlin.stdlib)
        implementation(deps.mapbox.androidSdk)
    }

    sourceSets["androidTest"].dependencies {
        implementation(deps.kotlin.test.junit)
        implementation(deps.kotlin.test.annotationsCommon)
    }

    sourceSets["jsMain"].dependencies {
        implementation(deps.kotlin.stdlibJs)
    }

    sourceSets["jsTest"].dependencies {
        implementation(deps.kotlin.test.js)
        implementation(deps.kotlin.test.annotationsCommon)
    }
}
