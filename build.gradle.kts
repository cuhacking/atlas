// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        // https://github.com/cashapp/sqldelight/pull/2224
        maven(url = "https://www.jetbrains.com/intellij-repository/releases")
        maven(url = "https://jetbrains.bintray.com/intellij-third-party-dependencies")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-beta03")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.32")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.0")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.7.0")
        classpath("com.github.node-gradle:gradle-node-plugin:2.2.4")
        classpath("com.github.jengelman.gradle.plugins:shadow:6.1.0")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt") version "1.15.0" apply false
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()

        // kotlinx-datetime
        maven(url = "https://kotlin.bintray.com/kotlinx/")

        maven(url = "https://oss.sonatype.org/content/repositories/snapshots")
        maven(url = "https://api.mapbox.com/downloads/v2/releases/maven") {
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                // Do not change the username below.
                // This should always be `mapbox` (not your username).
                username = "mapbox"
                // Use the secret token you stored in gradle.properties as the password
                password = (project.properties["MAPBOX_DOWNLOADS_TOKEN"] ?: "") as String?
            }
        }
    }
}

tasks.create("displayVersionInfo", DisplayVersionInfo::class)
