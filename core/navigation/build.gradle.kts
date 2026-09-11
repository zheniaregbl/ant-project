plugins {
    id("ant.kmp.compose")
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(libs.navigation.compose)
    }
}
