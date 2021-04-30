import org.gradle.api.provider.Provider

fun Provider<String>.getInt(): Int {
    return get().toInt()
}
