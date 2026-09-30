plugins {
    id("ant.kmp.library")
    id("ant.kmp.room")
    id("ant.kmp.koin")
}

kotlin {
    sourceSets.androidMain.dependencies {
        api(libs.koin.android)
    }
}
