plugins {
    id("ant.kmp.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(project(":core:domain"))
        implementation(project(":core:database"))
        api(libs.room.runtime)
        implementation(libs.sqlite.bundled)
    }
}
