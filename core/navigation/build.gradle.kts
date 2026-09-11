plugins {
    id("ant.kmp.library")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.navigation.compose)
    }
}
