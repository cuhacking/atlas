import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Properties

@Suppress("ForbiddenComment", "MaxLineLength", "MaximumLineLength")
plugins {
    id("com.android.library")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.squareup.sqldelight")
    id("com.codingfeline.buildkonfig")
    id("io.gitlab.arturbosch.detekt")
}

repositories {
    maven(url = "https://kotlin.bintray.com/native-xcode")
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

version = "1.0"

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

                val mapboxEventsPath = "${buildDir.absolutePath}/cocoapods/synthetic/IOS/common/build/Release-iphonesimulator/MapboxMobileEvents"
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

        ios.deploymentTarget = libs.versions.ios.get()
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
        implementation(libs.sqldelight.runtime)
        implementation(libs.sqldelight.coroutines)
        implementation(libs.bundles.spatialk)
        implementation(libs.ktor.client.core)
        implementation(libs.kotlinx.datetime)
        implementation(libs.klock)
        api(libs.mmapp)

        // Ensure multithreaded coroutine dependency is used to resolve ktor issue on ios
        // https://kotlinlang.org/docs/mobile/concurrency-and-coroutines.html#multithreaded-coroutines
        implementation(libs.kotlinx.coroutines.get().module.toString()) {
            version {
                strictly(libs.versions.coroutines.get())
            }
        }
    }

    sourceSets["commonTest"].dependencies {
        implementation(kotlin("test-common"))
        implementation(kotlin("test-annotations-common"))
        implementation(libs.ktor.client.mock)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.serialization)
    }

    sourceSets["androidMain"].dependencies {
        implementation(libs.sqldelight.driver.android)
        api(libs.mapbox.android)
        implementation(libs.ktor.client.okhttp)
    }

    sourceSets["androidTest"].dependencies {
        implementation(kotlin("test-junit"))
        implementation(libs.sqldelight.driver.jvm)
    }

    sourceSets["iosMain"].dependencies {
        implementation(libs.sqldelight.driver.native)
        implementation(libs.ktor.client.ios)
    }

    sourceSets["jsMain"].dependencies {
        implementation(libs.ktor.client.js)
        implementation(libs.sqldelight.driver.js)
        // TODO: Check if the dependencies below are still needed after upgrading Kotlin and/or Ktor
        implementation(npm("abort-controller", "3.0.0"))
        implementation(npm("node-fetch", "2.6.1"))
    }

    sourceSets["jsTest"].dependencies {
        implementation(kotlin("test-js"))
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

    val props = Properties()
    val localPropsFile = project.rootProject.file("local.properties")
    if (localPropsFile.exists()) {
        props.load(localPropsFile.inputStream())
    }

    // default config is required
    defaultConfigs {
        if (props.containsKey("mapbox.key")) {
            buildConfigField(
                STRING, "MAPBOX_KEY", props.getProperty("mapbox.key")
            )
        } else {
            throw GradleException("mapbox.key not declared in local.properties")
        }

        if (props.containsKey("server.url")) {
            buildConfigField(
                STRING, "SERVER_URL", props.getProperty("server.url")
            )
        } else {
            throw GradleException("server.url not declared in local.properties")
        }
    }

    // Configure js target server url
    targetConfigs(closureOf<NamedDomainObjectContainer<TargetConfigDsl>> {
        create("js") {
            if (props.containsKey("server.web.url")) {
                buildConfigField(
                    STRING, "SERVER_URL", props.getProperty("server.web.url")
                )
            } else {
                throw GradleException("server.web.url not declared in local.properties")
            }
        }
    })
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
    withType<Detekt> {
        jvmTarget = "11"
    }
    // TODO: Fix concurrency freezing issue
    getByName("iosTest") {
        enabled = false
    }
}

apply(plugin = "com.cuhacking.mmapp")
