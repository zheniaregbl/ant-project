plugins {
    id("ant.kmp.library")
    id("ant.kmp.room")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(project(":core:domain"))
    }
}
