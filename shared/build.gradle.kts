plugins {
    id("ant.kmp.feature")
}

kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }
    sourceSets.commonMain.dependencies {
        implementation(project(":feature:tasks"))
    }
}
