/*
 *  Copyright 2011 The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 * Copyright (c) 2023. CarePat All right reserved.
 * This software is the proprietary information of CarePet Co.,Ltd.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-pet.kr             2023. 9. 12.   description...
 */

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "kr.carepet.gpx"
    compileSdk = 33

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // -----------------------------------------------------------------------------------------
        // We use the Semantic Versioning (https://semver.org/):
        // -----------------------------------------------------------------------------------------
        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf("eventBusIndex" to "eu.basicairdata.graziano.gpslogger.EventBusIndex"))
            }
        }
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
}

dependencies {
    //implementation("com.github.bumptech.glide:glide:4.15.1")    //https://github.com/BasicAirData/GPSLogger
    //implementation("org.greenrobot:eventbus:3.3.1") //https://github.com/BasicAirData/GPSLogger
    //annotationProcessor("org.greenrobot:eventbus-annotation-processor:3.3.1")   //https://github.com/BasicAirData/GPSLogger

    // JodaTime for Android
    // https://github.com/dlew/joda-time-android
    implementation("net.danlew:android.joda:2.12.1.1")  //https://github.com/ticofab/android-gpx-parser
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")    //https://github.com/RideBeeline/android-rx-gpx-writer
    implementation("org.osmdroid:osmdroid-android:6.1.10")  //https://github.com/osmdroid/osmdroid

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}