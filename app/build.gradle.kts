import java.io.FileInputStream
import java.util.Properties

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
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"


        testInstrumentationRunner = "fr.ilardi.eventorias.CustomHiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        val MAPS_API_KEY : String
        if (localPropertiesFile.exists()) {
            FileInputStream(localPropertiesFile).use { stream ->
                localProperties.load(stream)
            }
            MAPS_API_KEY = localProperties["MAPS_API_KEY"]?.toString().toString()
        } else {
            MAPS_API_KEY=""
        }

        buildConfigField("String", "MAPS_API_KEY", "\"$MAPS_API_KEY\"")
        manifestPlaceholders["MAPS_API_KEY"] = MAPS_API_KEY
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/DEPENDENCIES"
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
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.7.7")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation ("androidx.compose.ui:ui-test-manifest:1.7.7")
    implementation ("androidx.appcompat:appcompat:1.7.0")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.5.0")


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
    implementation ("com.google.android.libraries.places:places:4.1.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("io.coil-kt:coil-compose:2.7.0")

    testImplementation ("org.mockito:mockito-core:5.5.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    testImplementation ("io.mockk:mockk:1.13.4")
    androidTestImplementation("io.mockk:mockk-android:1.13.5")

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