import androidx.room.gradle.RoomExtension
import ext.lib
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("androidx.room")

        extensions.configure<RoomExtension> {
            schemaDirectory("$projectDir/schemas")
        }

        val compiler = libs.lib("room-compiler")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.commonMain.dependencies {
                implementation(libs.lib("room-runtime"))
                implementation(libs.lib("sqlite-bundled"))
            }
            sourceSets.commonTest.dependencies {
                implementation(libs.lib("room-testing"))
            }

            targets.configureEach {
                if (targetName != "metadata") {
                    val configuration = "ksp${targetName.replaceFirstChar { it.uppercase() }}"
                    target.dependencies.add(configuration, compiler)
                }
            }
        }
    }
}
