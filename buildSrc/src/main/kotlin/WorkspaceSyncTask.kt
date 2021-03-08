import com.squareup.moshi.Moshi
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File

@Suppress("UNCHECKED_CAST")
open class WorkspaceSyncTask : DefaultTask() {
    private val moshi = Moshi.Builder()
        .build()
    private val adapter = moshi.adapter(Map::class.java).indent("    ")

    @get:Input
    var dependencies = listOf<String>()

    /**
     * Reads a `package.json` file and returns it as a `Map<String, Any?>?` or `null` if not found.
     */
    private fun fromSrcPackageJson(packageJson: File?): MutableMap<String, Any?>? =
        packageJson?.readText()?.let {
            adapter.fromJson(it)
        } as MutableMap<String, Any?>?

    /**
     * Reads the `dependencies` block from a `package.json` file corresponding to a Kotlin/JS package
     * with the given [packageName].
     * The generated package name usually follows the form `rootProjectName-moduleName`.
     */
    private fun getPackageDependencies(packageName: String): List<String> {
        val packageDir = project.rootDir.resolve("build/js/packages/$packageName")
        val packageJson = fromSrcPackageJson(packageDir.resolve("package.json"))
            ?: throw kotlin.IllegalStateException("Could not find package.json file for $packageName")

        val dependencies = packageJson["dependencies"] as Map<String, String>?
            ?: throw IllegalStateException("Could not get dependencies")

        val workspaceList = dependencies.filter { (_, path) -> !path[0].isDigit() }
            .map { (_, path) -> path.removePrefix("file:") }
            .map { path -> File(path).relativeTo(project.projectDir).path.replace("\\", "/") }

        return workspaceList + packageDir.relativeTo(project.projectDir).path.replace("\\", "/")
    }

    private fun MutableMap<String, Any?>.saveTo(file: File) {
        file.writeText(adapter.toJson(this))
    }

    @TaskAction
    fun sync() {
        val jsonFile = project.projectDir.resolve("package.json")
        val packageJson = fromSrcPackageJson(jsonFile)
            ?: throw kotlin.IllegalStateException("Could not find package.json for ${project.name}")
        val workspaces = dependencies.flatMap { getPackageDependencies(it) }

        packageJson["workspaces"] = workspaces.toSet()
        packageJson.saveTo(jsonFile)
    }
}
