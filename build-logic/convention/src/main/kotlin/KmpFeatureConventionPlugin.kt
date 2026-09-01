import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import ext.bundle
import ext.lib
import ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        pluginManager.apply("ant.kmp.library")
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

        extensions.configure<KotlinMultiplatformExtension> {

            (this as ExtensionAware).extensions
                .configure<KotlinMultiplatformAndroidLibraryExtension>("android") {
                    androidResources { enable = true }
                }

            sourceSets.commonMain.dependencies {
                implementation(libs.bundle("compose-common"))
                implementation(libs.bundle("koin-common"))
                implementation(libs.lib("navigation-compose"))

                implementation(project(":core:domain"))
            }

            sourceSets.androidMain.dependencies {
                implementation(libs.lib("compose-uiTooling"))
            }

//            sourceSets.commonTest.dependencies {
//                implementation(project(":core:testing"))
//            }
        }

        dependencies {
            add("androidRuntimeClasspath", libs.lib("compose-uiTooling"))
        }
    }
}
