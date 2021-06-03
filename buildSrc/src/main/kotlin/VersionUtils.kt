import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.BufferedReader
import java.io.InputStreamReader

fun generateVersionCode(): Int = "git rev-list --first-parent --count HEAD".execute().text.trim().toInt()
fun generateVersionName(): String = "git describe --tag".execute().text.trim()

private fun String.execute(): Process = Runtime.getRuntime().exec(this)


open class DisplayVersionInfo : DefaultTask() {
    @TaskAction
    fun display() {
        println("Version Code: ${generateVersionCode()}")
        println("Version Name: ${generateVersionName()}")
    }
}

private val Process.text: String get() = BufferedReader(InputStreamReader(inputStream)).readLine()

