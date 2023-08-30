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
 *  isyuun@care-pet.kr             2023. 8. 25.   description...
 */

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
//plugins {
//    alias(libs.plugins.com.android.library)
//    alias(libs.plugins.kotlin.android)
//}

android {
    namespace = "kr.carepet.gps"
    compileSdk = 33

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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    buildFeatures {
        buildConfig = true
    }
}

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
    /** <-- samples --> */
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    /** <-- //samples --> */
}

dependencies {
    /** Define the samples to load */
    implementation(libs.play.services.location)
    implementation(libs.kotlin.coroutines.play)
    implementation(libs.androidx.work.runtime.ktx)
    //implementation(project(":samples:base"))
    implementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    implementation(libs.casa.base)
    ksp(libs.casa.processor)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.core)
    //implementation(libs.androidx.fragment)
    //implementation(libs.androidx.activity.compose)
    implementation(libs.compose.foundation.foundation)
    //implementation(libs.compose.runtime.runtime)
    implementation(libs.compose.runtime.livedata)
    //implementation(libs.androidx.lifecycle.viewmodel.compose)
    //implementation(libs.compose.ui.ui)
    implementation(libs.compose.material3)
    //implementation(libs.coil.compose)
    //implementation(libs.coil.video)
    implementation(libs.accompanist.permissions)
    //implementation(libs.compose.ui.tooling.preview)
    //debugImplementation(libs.compose.ui.tooling)
    //androidTestImplementation(libs.androidx.test.core)
    //androidTestImplementation(libs.androidx.test.runner)
    /** Define the samples to load */

    implementation(project(mapOf("path" to ":_APP")))
    implementation(project(mapOf("path" to ":GPSLogger")))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}