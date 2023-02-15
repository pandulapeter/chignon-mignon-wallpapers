plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.android.material)
    implementation(libs.kotlin.coroutines)
}

android {
    compileSdk = System.getProperty("TARGET_SDK_VERSION").toInt()
    defaultConfig.minSdk = System.getProperty("MIN_SDK_VERSION").toInt()
    kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures.dataBinding = true
    namespace = "com.chignonMignon.wallpapers.presentation.utilities"
}