import com.moowork.gradle.node.yarn.YarnTask

apply(plugin = "com.github.node-gradle.node")

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
