plugins {

    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.outfitmatch"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.outfitmatch"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}


dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.inappmessaging)
    implementation(libs.cronet.embedded)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)


    implementation (libs.gson) // Para guardar favoritos

    implementation (libs.recyclerview) // o la versión más reciente
    implementation (libs.constraintlayout.v214) // o la versión más reciente

    // https://mvnrepository.com/artifact/com.yuyakaido.android/card-stack-view
    implementation (libs.cardstackview)
    implementation (libs.firebase.storage)

    implementation (libs.material.v190)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // HTTP client para solicitudes a APIs (OpenWeatherMap)
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

// JSON parsing
    implementation ("org.json:json:20210307")

// Biblioteca para obtener la ubicación del dispositivo
    implementation ("com.google.android.gms:play-services-location:21.0.1")

// Glide (ya lo tienes para cargar el GIF)
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

//Lottie
    implementation ("com.airbnb.android:lottie:6.1.0")


}