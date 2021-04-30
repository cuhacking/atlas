import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("io.gitlab.arturbosch.detekt")
}

android {
    compileSdk = libs.versions.compileSdk.getInt()

    defaultConfig {
        targetSdk = libs.versions.compileSdk.getInt()
        minSdk = libs.versions.minSdk.getInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
        // Workaround for KMP looking for `actual` declarations in androidAndroidTest
        getByName("androidTest") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
            manifest.srcFile("src/androidTest/AndroidManifest.xml")
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "11"
    }

    // Workaround for: https://youtrack.jetbrains.com/issue/KT-43944
    configurations {
        create("androidTestApi")
        create("androidTestDebugApi")
        create("androidTestReleaseApi")
        create("testApi")
        create("testDebugApi")
        create("testReleaseApi")
    }
}

version = Versions.atlas

kotlin {
    android()
    js(IR) {
        browser()

        compilations["main"].packageJson {
            customField("types", "kotlin/Atlas-mapbox.d.ts")
        }
    }

    // Select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64 else ::iosX64

    iOSTarget("ios") {}

    cocoapods {
        summary = "MapboxAtlas framework"
        homepage = "https://github.com/cuhacking/atlas"

        frameworkName = "MapboxAtlas"
        podfile = project.file("../ios/Podfile")

        ios.deploymentTarget = Versions.ios

        pod("Mapbox-iOS-SDK", "~> 5.9", moduleName = "Mapbox")
    }

    sourceSets["commonMain"].dependencies {
        api(libs.bundles.spatialk)
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
    }

    sourceSets["androidMain"].dependencies {
        implementation(libs.mapbox.android)
    }

    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test-junit"))
    }

    sourceSets["jsMain"].dependencies {
    }

    sourceSets["jsTest"].dependencies {
        implementation(kotlin("test-js"))
    }
}

detekt {
    failFast = true
    buildUponDefaultConfig = true
    config = files("${rootProject.projectDir}/detekt.yml")
    input = files("$projectDir/src")
}

tasks {
    withType<Detekt> {
        jvmTarget = "11"
    }
}
