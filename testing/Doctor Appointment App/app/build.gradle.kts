plugins {
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.googleServices)

    kotlin("kapt")
}

android {

    namespace = "com.example.doctorappointmentapp"

    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.doctorappointmentapp"
        minSdk = 24
        targetSdk = 35
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


    buildFeatures {
        viewBinding = true
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
    implementation(libs.glide)

    implementation("androidx.cardview:cardview:1.0.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Firebase BoM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ai)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    implementation(libs.material)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}