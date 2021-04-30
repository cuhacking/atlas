import com.moowork.gradle.node.yarn.YarnTask

plugins {
    id("com.github.node-gradle.node")
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
