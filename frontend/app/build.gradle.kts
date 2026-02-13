plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")

    //Firebase
    id("com.google.gms.google-services")
}

android {
    namespace = "com.matrix.appmobilechimera"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.matrix.appmobilechimera"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dependencias agregadas
    // Retrofit para conectar con el Backend de Python
    implementation(libs.retrofit.main)
    implementation(libs.retrofit.gson)

    // Google Auth para el inicio de sesión
    implementation(libs.google.auth)

    // Glide para manejo de imágenes
    implementation(libs.glide.main)

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.google.firebase:firebase-analytics")

    //Recyclerviwe
    implementation(libs.androidx.recyclerview)

    //Habilita Multipart y extensiones de archivos
    implementation(libs.okhttp.main)
}