plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
    alias(libs.plugins.hilt)
    kotlin("kapt")

}

android {
    namespace = "fr.ilardi.eventorias"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.ilardi.eventorias"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val mapsApiKey: String = project.findProperty("MAPS_API_KEY") as String? ?: ""
        buildConfigField("String", "MAPS_API_KEY", "\"$mapsApiKey\"")
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.appcompat:appcompat:1.7.0")


    //FIREBASE
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.firebase.firestore.ktx)
    implementation (libs.firebase.ui.auth)
    implementation(libs.firebase.ui.storage)
    implementation(libs.firebase.ui.firestore)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.messaging)



    //HILT
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    testImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)

    implementation("io.coil-kt:coil-compose:2.7.0")


    //GOOGLE MAP
    implementation("com.google.maps.android:maps-compose:4.4.2")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.maps.android:maps-compose-widgets:4.4.2")

}

kapt {
    arguments {
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
    }
}
android {
    buildFeatures {
        buildConfig = true
    }
}