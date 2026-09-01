plugins {
    id("com.android.library")
}

dependencies {
    implementation(project(":data:model"))
    implementation(project(":domain"))
    implementation(project(":presentation:shared"))
    implementation(project(":presentation:debugMenu"))
    implementation(project(":presentation:utilities"))
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.recyclerview)
    implementation(libs.google.android.material)
    implementation(libs.koin.android)
    implementation(libs.kotlin.coroutines)
}

android {
    compileSdk = System.getProperty("TARGET_SDK_VERSION").toInt()
    defaultConfig.minSdk = System.getProperty("MIN_SDK_VERSION").toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig = true
        dataBinding = true
    }
    namespace = "com.chignonMignon.wallpapers.presentation.collectionDetails"
}