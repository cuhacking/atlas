import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import java.util.*

plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("com.squareup.sqldelight")
    id("com.codingfeline.buildkonfig")
}

repositories {
    maven(url = "https://kotlin.bintray.com/native-xcode")
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
        exclude("META-INF/*.kotlin_module")
    }
}

kotlin {
    //select iOS target platform depending on the Xcode environment variables
    val iOSTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
        if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true)
            ::iosArm64
        else
            ::iosX64

    iOSTarget("ios") {
        binaries {
            framework {
                baseName = "Common"
            }
        }
    }

    android()

    js {
        useCommonJs()
        browser()
    }

    sourceSets["commonMain"].dependencies {
        implementation(deps.kotlin.stdlibCommon)
        implementation(deps.sqldelight.runtime)
        implementation(deps.spatialk.geojson)
        implementation(deps.spatialk.turf)
        implementation(deps.spatialk.geojsonDsl)
        implementation (deps.ktor.commonDriver)
    }

    sourceSets["androidMain"].dependencies {
        implementation(deps.kotlin.stdlib)
        implementation(deps.sqldelight.androidDriver)
        implementation(deps.mapbox.androidSdk)
        implementation(deps.ktor.androidDriver)
    }

    sourceSets["iosMain"].dependencies {
        implementation(deps.kotlin.xcode)
        implementation(deps.sqldelight.nativeDriver)
        implementation(deps.ktor.iosDriver)
    }

    sourceSets["jsMain"].dependencies {
        implementation(deps.kotlin.stdlibJs)
        implementation (deps.ktor.jsDriver)
        //! https://github.com/cashapp/sqldelight/issues/1667
        // implementation(deps.sqldelight.javascriptDriver)
    }
}

buildkonfig {
    packageName = "com.cuhacking.atlas"

    defaultConfigs {
        val props = Properties()
        val localPropsFile = project.rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            props.load(localPropsFile.inputStream())
        }

        if (props.containsKey("mapbox.key")) {
            buildConfigField(com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING, "MAPBOX_KEY", "\"${props.getProperty("mapbox.key")}\"")
        } else {
            throw GradleException("mapbox.key not declared in local.properties")
        }
    }
}

val packForXCode by tasks.creating(Sync::class) {
    val targetDir = File(buildDir, "xcode-frameworks")

    /// selecting the right configuration for the iOS
    /// framework depending on the environment
    /// variables set by Xcode build
    val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
    val framework = kotlin.targets
        .getByName<KotlinNativeTarget>("ios")
        .binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)

    from({ framework.outputDirectory })
    into(targetDir)

    /// generate a helpful ./gradlew wrapper with embedded Java path
    doLast {
        val gradlew = File(targetDir, "gradlew")
        gradlew.writeText(
            "#!/bin/bash\n"
                    + "export 'JAVA_HOME=${System.getProperty("java.home")}'\n"
                    + "cd '${rootProject.rootDir}'\n"
                    + "./gradlew \$@\n"
        )
        gradlew.setExecutable(true, false)
    }
}

tasks.getByName("build").dependsOn(packForXCode)

sqldelight {
    database("Database") {
        packageName = "com.cuhacking.atlas.db"
    }
}