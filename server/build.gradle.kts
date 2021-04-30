plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

kotlin {
    dependencies {
        implementation(libs.ktor.server.core)
        implementation(libs.ktor.server.cio)
        implementation(libs.logback)
        implementation(libs.clikt)
        implementation(libs.spatialk.geojson)

        testImplementation(libs.ktor.server.test)
        testImplementation(libs.junit)
        testImplementation(kotlin("test-junit"))
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClassName = "com.cuhacking.atlas.server.ServerKt"
}
