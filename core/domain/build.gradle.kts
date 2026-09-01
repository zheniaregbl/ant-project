plugins {
    id("ant.kmp.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.kotlinx.datetime)
    }
}
