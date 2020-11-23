import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

kotlin {
    dependencies {
        implementation(deps.ktor.server.core)
        implementation(deps.ktor.server.netty)
        implementation(deps.slf4j)
        implementation(deps.clikt)
        implementation(deps.spatialk.geojson)

        testImplementation(deps.ktor.server.test)
        testImplementation(deps.junit)
        testImplementation(deps.kotlin.test.junit)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "com.cuhacking.atlas.server.ServerKt"
}
