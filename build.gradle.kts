plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

val stagedFiles: Set<String>? =
    providers.gradleProperty("stagedFiles").orNull
        ?.split(",")
        ?.filter { it.isNotBlank() }
        ?.map { rootProject.file(it).absolutePath.replace('\\', '/') }
        ?.toSet()

fun Set<String>.containsFile(file: File) = file.absolutePath.replace('\\', '/') in this

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    val staged = stagedFiles

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        buildUponDefaultConfig = true
        parallel = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        baseline = file("detekt-baseline.xml")

        source.setFrom(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/androidMain/kotlin",
            "src/iosMain/kotlin",
            "src/jvmMain/kotlin",
            "src/desktopMain/kotlin",
        )
    }

    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        filter {
            val buildDir = layout.buildDirectory.get().asFile
            exclude { element -> element.file.startsWith(buildDir) }
            if (staged != null) {
                exclude { element ->
                    !element.isDirectory &&
                            element.file.absolutePath.replace('\\', '/') !in staged
                }
            }
        }
    }

    if (staged != null) {
        tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
            exclude { element ->
                !element.isDirectory &&
                        element.file.absolutePath.replace('\\', '/') !in staged
            }
        }
    }
}

tasks.register<Exec>("installGitHooks") {
    commandLine("git", "config", "core.hooksPath", ".githooks")
}
