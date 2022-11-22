plugins {
    id("kotlin")
}

dependencies {
    api(project(":data:model"))
    implementation(libs.koin.core)
    implementation(libs.kotlin.coroutines)
}