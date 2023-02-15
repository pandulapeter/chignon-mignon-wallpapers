plugins {
    id("com.android.library")
    id("kotlin-android")
}

dependencies {
    implementation(project(":data:model"))
    implementation(libs.androidx.appcompat)
    debugImplementation(libs.beagle)
    debugImplementation(libs.beagle.crashLogger)
}

android {
    compileSdk = System.getProperty("TARGET_SDK_VERSION").toInt()
    defaultConfig.minSdk = System.getProperty("MIN_SDK_VERSION").toInt()
    kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    namespace = "com.chignonMignon.wallpapers.presentation.debugMenu"
}