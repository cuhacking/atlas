// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        // Temporary workaround: https://github.com/cashapp/sqldelight/issues/2058
        classpath("xml-apis:xml-apis:1.4.01")
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
