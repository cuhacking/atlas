import com.moowork.gradle.node.yarn.YarnTask

plugins {
    id("com.github.node-gradle.node")
}

node {
    download = true
    version = "14.15.4"
}

tasks {
    create<YarnTask>("build") {
        group = "build"
        args = listOf("run", "build")

        dependsOn("yarn")
    }

    create<Delete>("clean") {
        group = "build"

        delete("$projectDir/build")
        delete("$projectDir/node_modules")
    }
}
