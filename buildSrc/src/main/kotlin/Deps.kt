@file:Suppress("ClassName")

object deps {
    object plugins {
        const val android = "com.android.tools.build:gradle:4.2.0-alpha03"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
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
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
        const val stdlibCommon = "org.jetbrains.kotlin:kotlin-stdlib-common:${Versions.kotlin}"
        const val xcode = "org.jetbrains.kotlin.native.xcode:kotlin-native-xcode-11-4-workaround:1.3.72.0"
    }

    const val material = "com.google.android.material:material:1.1.0"
}