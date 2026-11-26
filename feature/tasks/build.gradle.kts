plugins {
    id("ant.kmp.feature")
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.datetime.wheel.picker)
    }
}
