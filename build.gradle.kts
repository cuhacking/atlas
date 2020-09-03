// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(deps.plugins.android)
        classpath(deps.plugins.kotlin)
        classpath(deps.plugins.sqldelight)
        classpath(deps.plugins.buildKonfig)
        classpath(deps.plugins.node)

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
    }
}
