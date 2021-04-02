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
        classpath(deps.plugins.android)
        classpath(deps.plugins.kotlin)
        classpath(deps.plugins.sqldelight)
        classpath(deps.plugins.buildKonfig)
        classpath(deps.plugins.node)
        classpath(deps.plugins.shadow)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

plugins {
    id(deps.plugins.detekt) version Versions.detekt apply false
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
