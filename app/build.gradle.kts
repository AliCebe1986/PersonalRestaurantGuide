plugins {
    alias(libs.plugins.android.application)
    // Kotlin Kapt eklentisi, ancak projeniz Java olduğu için gerek yok
}

android {
    namespace = "ca.gbc.comp3074.personalrestaurantguide"
    compileSdk = 34

    defaultConfig {
        applicationId = "ca.gbc.comp3074.personalrestaurantguide"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.0.2")

    // Facebook SDK
    implementation("com.facebook.android:facebook-android-sdk:[8,9)")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // Room Database
    implementation("androidx.room:room-runtime:2.4.2")
    annotationProcessor("androidx.room:room-compiler:2.4.2")

    // Gson 
    implementation("com.google.code.gson:gson:2.8.8")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.facebook.android:facebook-share:[8,9)")
}
