plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

dependencies {
    implementation(project(":data:model"))
    implementation(project(":domain"))
    implementation(libs.androidx.appcompat)
    implementation(libs.coil)
    implementation(libs.google.android.material)
    implementation(libs.koin.core)
}

android {
    val targetSdkVersion = System.getProperty("TARGET_SDK_VERSION").toInt()
    compileSdk = targetSdkVersion
    kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dataBinding.enable = true
}