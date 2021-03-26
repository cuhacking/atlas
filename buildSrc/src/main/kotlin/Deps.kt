@file:Suppress("ClassName", "ClassNaming", "Filename", "MatchingDeclarationName")

object deps {
    object plugins {
        const val android = "com.android.tools.build:gradle:7.0.0-alpha11"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val sqldelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}"
        const val buildKonfig = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.7.0"
        const val node = "com.github.node-gradle:gradle-node-plugin:2.2.4"
        const val shadow = "com.github.jengelman.gradle.plugins:shadow:6.1.0"

        // Uses newer plugins DSL
        const val detekt = "io.gitlab.arturbosch.detekt"
    }

    object androidx {
        const val core = "androidx.core:core-ktx:1.3.2"
        const val appCompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val lifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.0-rc01"

        const val junit = "androidx.test.ext:junit:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    }

    const val junit = "junit:junit:4.13"

    object kotlin {
        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2"
        const val datetime = "org.jetbrains.kotlinx:kotlinx-datetime:0.1.1"

        object test {
            const val common = "org.jetbrains.kotlin:kotlin-test-common"
            const val annotationsCommon = "org.jetbrains.kotlin:kotlin-test-annotations-common"
            const val junit = "org.jetbrains.kotlin:kotlin-test-junit"
            const val js = "org.jetbrains.kotlin:kotlin-test-js"
        }
    }

    object sqldelight {
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqldelight}"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqldelight}"
        const val jsDriver = "com.squareup.sqldelight:sqljs-driver:${Versions.sqldelight}"
        const val jsRuntimeDriver = "com.squareup.sqldelight:runtime-js:${Versions.sqldelight}"
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqldelight}"
        const val sqliteDriver = "com.squareup.sqldelight:sqlite-driver:${Versions.sqldelight}"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqldelight}"
    }

    object ktor {
        const val androidDriver = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val iosDriver = "io.ktor:ktor-client-ios:${Versions.ktor}"
        const val jsDriver = "io.ktor:ktor-client-js:${Versions.ktor}"
        const val commonDriver = "io.ktor:ktor-client-core:${Versions.ktor}"
        const val mockClient = "io.ktor:ktor-client-mock:${Versions.ktor}"
        const val jsonFeature = "io.ktor:ktor-client-json:${Versions.ktor}"
        const val jsonSerializer = "io.ktor:ktor-client-serialization:${Versions.ktor}"

        object server {
            const val core = "io.ktor:ktor-server-core:${Versions.ktor}"
            const val netty = "io.ktor:ktor-server-netty:${Versions.ktor}"
            const val test = "io.ktor:ktor-server-test-host:${Versions.ktor}"
        }
    }

    object spatialk {
        const val geojson = "io.github.dellisd.spatialk:geojson:${Versions.spatialk}"
        const val turf = "io.github.dellisd.spatialk:turf:${Versions.spatialk}"
        const val geojsonDsl = "io.github.dellisd.spatialk:geojson-dsl:${Versions.spatialk}"
    }

    object mapbox {
        const val androidSdk = "com.mapbox.mapboxsdk:mapbox-android-sdk:9.6.0"
    }

    const val material = "com.google.android.material:material:1.3.0-beta01"
    const val turbine = "app.cash.turbine:turbine:0.3.0"
    const val slf4j = "org.slf4j:slf4j-simple:1.7.9"
    const val clikt = "com.github.ajalt.clikt:clikt:3.0.1"
}
