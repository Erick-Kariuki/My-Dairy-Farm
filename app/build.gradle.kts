plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.techlad.mydairyfarm"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.techlad.mydairyfarm"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation("androidx.navigation:navigation-compose:2.9.5")

    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("io.coil-kt:coil-compose:2.7.0")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0") // Use the latest stable version

    // Gson Converter (for JSON serialization/deserialization)
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0") // Use the same version as Retrofit

    // OkHttp (Retrofit uses OkHttp for network requests)
    // You might also want to include the OkHttp logging interceptor for debugging
    implementation ("com.squareup.okhttp3:okhttp:4.12.0") // Use the latest stable version
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0") // Use the same version as OkHttp

    //Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.3.1")

}