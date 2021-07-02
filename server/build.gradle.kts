import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

repositories {
    maven(url = "https://jitpack.io")
}

kotlin {
    dependencies {
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.cio)
        implementation(libs.logback)
        implementation(libs.clikt)
        implementation(libs.spatialk.geojson)
        implementation(libs.json.schema)

        testImplementation(libs.ktor.server.test)
        testImplementation(libs.junit)
        testImplementation(kotlin("test-junit"))
    }
}

tasks.withType(KotlinCompile::class) {
    kotlinOptions.jvmTarget = "11"
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "com.cuhacking.atlas.server.ServerKt"
}
