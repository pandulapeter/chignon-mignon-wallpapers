plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp") version libs.versions.kotlin.ksp.get()
}

dependencies {
    implementation(project(":data:model"))
    implementation(project(":domain"))
    implementation(project(":presentation:debugMenu"))
    implementation(project(":presentation:utilities"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recyclerview)
    api(libs.androidx.swiperefreshlayout)
    implementation(libs.bumptech.glide)
    ksp(libs.bumptech.glide.codegen)
    implementation(libs.bumptech.glide.okhttp)
    implementation(libs.google.android.material)
    implementation(libs.koin.android)
    implementation(libs.kotlin.coroutines)
    implementation(libs.square.okhttp)
}

android {
    compileSdk = System.getProperty("TARGET_SDK_VERSION").toInt()
    defaultConfig.minSdk = System.getProperty("MIN_SDK_VERSION").toInt()
    kotlinOptions.jvmTarget = libs.versions.jvm.target.get()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    namespace = "com.chignonMignon.wallpapers.presentation.shared"
}