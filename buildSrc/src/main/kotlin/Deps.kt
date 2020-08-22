@file:Suppress("ClassName")

object deps {
    object plugins {
        const val android = "com.android.tools.build:gradle:4.2.0-alpha07"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
        const val sqldelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}"
        const val buildKonfig = "com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.6.0"
    }

    object androidx {
        const val core = "androidx.core:core-ktx:1.3.0"
        const val appCompat = "androidx.appcompat:appcompat:1.1.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"

        const val junit = "androidx.test.ext:junit:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:3.2.0"
    }

    const val junit = "junit:junit:4.13"

    object kotlin {
        const val junit = "org.jetbrains.kotlin:kotlin-test-junit"
    }

    object sqldelight {
        const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqldelight}"
        const val nativeDriver = "com.squareup.sqldelight:native-driver:${Versions.sqldelight}"
        const val javascriptDriver = "com.squareup.sqldelight:sqljs-driver:${Versions.sqldelight}"
        const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqldelight}"
    }

    object ktor {
        const val androidDriver = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
        const val iosDriver = "io.ktor:ktor-client-ios:${Versions.ktor}"
        const val jsDriver = "io.ktor:ktor-client-js:${Versions.ktor}"
        const val commonDriver = "io.ktor:ktor-client-core:${Versions.ktor}"
    }

    object spatialk {
        const val geojson = "io.github.dellisd.spatialk:geojson:0.0.2-SNAPSHOT"
        const val turf = "io.github.dellisd.spatialk:turf:0.0.2-SNAPSHOT"
        const val geojsonDsl = "io.github.dellisd.spatialk:geojson-dsl:0.0.2-SNAPSHOT"
    }

    object mapbox {
        const val androidSdk = "com.mapbox.mapboxsdk:mapbox-android-sdk:9.2.0"
    }

    const val material = "com.google.android.material:material:1.1.0"
}