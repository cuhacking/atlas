import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id(deps.plugins.detekt)
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

version = Versions.atlas

kotlin {
    android()
    js {
        browser()
    }

    // Select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64 else ::iosX64

    iOSTarget("ios") {}

    cocoapods {
        summary = "mapbox framework"
        homepage = "https://github.com/cuhacking/atlas"

        frameworkName = "Common"

        ios.deploymentTarget = Versions.ios

        pod("Mapbox-iOS-SDK", "~> 5.9", moduleName = "Mapbox")
    }

    sourceSets["commonMain"].dependencies {
        implementation(deps.kotlin.stdlibCommon)
        api(deps.spatialk.geojson)
        implementation(deps.spatialk.turf)
        implementation(deps.spatialk.geojsonDsl)
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

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/detekt.yml")
    input = files("$projectDir/src")
}

tasks {
    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "1.8"
    }
}
