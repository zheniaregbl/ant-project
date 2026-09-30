plugins {
    id("ant.kmp.library")
    id("ant.kmp.koin")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(project(":core:domain"))
        implementation(project(":core:database"))
        api(libs.room.runtime)
        implementation(libs.sqlite.bundled)
    }
}
