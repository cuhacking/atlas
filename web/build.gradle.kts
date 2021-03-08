import com.moowork.gradle.node.yarn.YarnTask

apply(plugin = "com.github.node-gradle.node")

val packagesImported = listOf<String>(
    "spatial-k-geojson-jsIr/${Versions.spatialk}",
    "spatial-k-geojson-dsl-jsIr/${Versions.spatialk}",
    "spatial-k-geojson-turf-jsIr/${Versions.spatialk}"/*,
    "kotlinx-serialization-kotlinx-serialization-core-jsIr/${Versions.serialization}"*/
)

tasks {
    create<YarnTask>("build") {
        group = "build"
        args = listOf("run", "build")

        dependsOn(":common:jsBrowserDevelopmentWebpack", "workspaceSync", "yarn")
    }

    create<Delete>("clean") {
        group = "build"

        delete("$projectDir/build")
        delete("$projectDir/node_modules")
    }

    create<WorkspaceSyncTask>("workspaceSync") {
        group = "build"
        dependencies = listOf("Atlas-common", "Atlas-mapbox")
        dependsOn(":common:jsBrowserDevelopmentWebpack")
    }
}
