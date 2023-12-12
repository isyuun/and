/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 8. 25.   description...
 */
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "net.pettip.gps"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    //buildFeatures {
    //    buildConfig = true
    //}
}

dependencies {
    implementation(project(mapOf("path" to ":_APP")))
    implementation(project(mapOf("path" to ":APP")))
    implementation(project(mapOf("path" to ":GPX")))
    implementation(project(mapOf("path" to ":NAV")))

    //implementation("androidx.compose.material:material-android:1.5.4")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.naver.maps:map-sdk:3.17.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("androidx.exifinterface:exifinterface:1.3.6")

    implementation(libs.androidx.appcompat)
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
    //implementation(libs.material)
    //implementation(libs.androidx.work.runtime.ktx)
    //implementation(libs.kotlin.coroutines.play)

    /** 컴포즈.기본...JetPack.Compose */
    implementation(libs.androidx.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.work.runtime.ktx)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.testManifest)
    /** 컴포즈.기본...JetPack.Compose */
}
