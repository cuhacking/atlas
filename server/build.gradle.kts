plugins {
    kotlin("jvm")
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
