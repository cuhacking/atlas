import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.Properties

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.codingfeline.buildkonfig")
    id(deps.plugins.detekt)
}

repositories {
    maven(url = "https://kotlin.bintray.com/native-xcode")
}

android {
    compileSdkVersion(Versions.compileSdk)

    defaultConfig {
        targetSdk = Versions.compileSdk
        minSdk = Versions.minSdk
    }

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
        // Workaround for KMP looking for `actual` declarations in androidAndroidTest
        getByName("androidTest") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
            manifest.srcFile("src/androidTest/AndroidManifest.xml")
        }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
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
    // Select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64 else ::iosX64

    iOSTarget("ios") {
        // Link cocoapods for test builds: https://youtrack.jetbrains.com/issue/KT-44857
        binaries {
            getTest("DEBUG").apply {
                val mapboxPath =
                    "${buildDir.absolutePath}/cocoapods/synthetic/IOS/common/Pods/Mapbox-iOS-SDK/dynamic"
                linkerOpts("-F$mapboxPath")
                linkerOpts("-rpath", mapboxPath)
                linkerOpts("-framework", "Mapbox")

                val mapboxEventsPath =
                    "${buildDir.absolutePath}/cocoapods/synthetic/IOS/common/build/Release-iphonesimulator/MapboxMobileEvents"
                linkerOpts("-F$mapboxEventsPath")
                linkerOpts("-rpath", mapboxEventsPath)
                linkerOpts("-framework", "MapboxMobileEvents")
            }
        }
    }

    cocoapods {
        summary = "Common framework"
        homepage = "https://github.com/cuhacking/atlas"
        podfile = project.file("../ios/Podfile")
        frameworkName = "Common"

        ios.deploymentTarget = Versions.ios
        pod("Mapbox-iOS-SDK", "~> 5.9", moduleName = "Mapbox")
    }

    android()

    js(IR) {
        browser {
            binaries.library()
            testTask {
                useMocha {
                    timeout = "5s"
                }
            }
        }
        compilations["main"].packageJson {
            customField("types", "Atlas-common.d.ts")
            peerDependencies["sql.js"] = "1.0.0"
        }
    }

    sourceSets["commonMain"].dependencies {
        implementation(deps.sqldelight.runtime)
        implementation(deps.sqldelight.coroutines)
        implementation(deps.spatialk.geojson)
        implementation(deps.spatialk.turf)
        implementation(deps.spatialk.geojsonDsl)
        implementation(deps.ktor.commonDriver)
        implementation(deps.kotlin.coroutines)
        implementation(deps.kotlin.datetime)
        api(project(":mapbox"))
    }

    sourceSets["commonTest"].dependencies {
        implementation(deps.kotlin.test.common)
        implementation(deps.kotlin.test.annotationsCommon)
        implementation(deps.ktor.mockClient)
        implementation(deps.ktor.jsonFeature)
        implementation(deps.ktor.jsonSerializer)
    }

    sourceSets["androidMain"].dependencies {
        implementation(deps.sqldelight.androidDriver)
        api(deps.mapbox.androidSdk)
        implementation(deps.ktor.androidDriver)
        implementation(deps.ktor.mockClient)
        implementation(deps.kotlin.datetime)
    }

    sourceSets["androidTest"].dependencies {
        implementation(deps.kotlin.test.junit)
        implementation(deps.sqldelight.sqliteDriver)
    }

    sourceSets["iosMain"].dependencies {
        implementation(deps.sqldelight.nativeDriver)
        implementation(deps.ktor.iosDriver)
        implementation(deps.kotlin.datetime)
    }

    sourceSets["jsMain"].dependencies {
        implementation(deps.ktor.jsDriver)
        implementation(deps.kotlin.datetime)
        implementation(deps.sqldelight.jsDriver)
        implementation(deps.sqldelight.jsRuntimeDriver)
    }

    sourceSets["jsTest"].dependencies {
        implementation(deps.kotlin.test.js)
    }

    sourceSets.all {
        with(languageSettings) {
            useExperimentalAnnotation("kotlin.js.ExperimentalJsExport")
        }
    }
}

buildkonfig {
    packageName = "com.cuhacking.atlas.common"
    exposeObjectWithName = "AtlasConfig"

    defaultConfigs {
        val props = Properties()
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }

        if (props.containsKey("mapbox.key")) {
            buildConfigField(
                com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "MAPBOX_KEY",
                props.getProperty("mapbox.key")
            )
        } else {
            throw GradleException("mapbox.key not declared in local.properties")
        }

        if (props.containsKey("server.url")) {
            buildConfigField(
                com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "SERVER_URL",
                props.getProperty("server.url")
            )
        } else {
            throw GradleException("server.url not declared in local.properties")
        }
    }
}

sqldelight {
    database("AtlasDatabase") {
        packageName = "com.cuhacking.atlas.db"
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
    // TODO: Fix concurrency freezing issue
    getByName("iosTest") {
        enabled = false
    }
}
