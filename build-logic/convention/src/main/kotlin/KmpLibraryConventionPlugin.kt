import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import ext.bundle
import ext.lib
import ext.libs
import ext.version
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {

        pluginManager.apply("org.jetbrains.kotlin.multiplatform")
        pluginManager.apply("com.android.kotlin.multiplatform.library")

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(21)

            jvm()
            iosArm64()
            iosSimulatorArm64()

            compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }

            (this as ExtensionAware).extensions
                .configure<KotlinMultiplatformAndroidLibraryExtension>("android") {

                    namespace = "com.nimain.antproject." + path
                        .removePrefix(":")
                        .replace(':', '.')
                        .replace('-', '_')

                    compileSdk = libs.version("android-compileSdk").toInt()
                    minSdk = libs.version("android-minSdk").toInt()

                    targets.configureEach {
                        compilations.configureEach {
                            compileTaskProvider.configure {
                                val opts = compilerOptions
                                if (opts is KotlinJvmCompilerOptions) {
                                    opts.jvmTarget.set(JvmTarget.JVM_11)
                                }
                            }
                        }
                    }

                    withHostTest { }
                }

            sourceSets.commonMain.dependencies {
                implementation(libs.lib("kotlinx-coroutines-core"))
            }
            sourceSets.commonTest.dependencies {
                implementation(libs.bundle("test-common"))
            }
        }
    }
}