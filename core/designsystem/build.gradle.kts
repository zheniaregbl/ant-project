plugins {
    id("ant.kmp.compose")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.coil.compose)
        implementation(libs.coil.svg)
    }
}
