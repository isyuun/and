//import org.jetbrains.kotlin.base.kapt3.KaptOptions
//import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
    //kotlin("kapt")
    //id("com.google.dagger.hilt.android")
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("../.APK/kr.carepet.debug.jks")
        }
        create("release") {
            storeFile = file("../.APK/kr.carepet.release.jks")
            storePassword = "is230710!!"
            keyAlias = "carepet"
            keyPassword = "is230710!!"
        }
    }
    buildTypes {
        getByName("debug") {
            //isMinifyEnabled = true
            //isDebuggable = true
            //signingConfig = signingConfigs.getByName("debug")
            //manifestPlaceholders["enableCrashlytics"] = "false"
            //extra.set("alwaysUpdateBuildId", false)
            //proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "retrofit2.pro", "proguard.cfg")
        }
        getByName("release") {
            //shrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs.getByName("release")
            manifestPlaceholders["enableCrashlytics"] = "true"
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro", "retrofit2.pro")
        }
    }

    namespace = "kr.carepet.app.navi"
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.carepet.app.navi"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    //buildTypes {
    //    release {
    //        isMinifyEnabled = false
    //        proguardFiles(
    //            getDefaultProguardFile("proguard-android-optimize.txt"),
    //            "proguard-rules.pro"
    //        )
    //    }
    //}
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
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(mapOf("path" to ":_APP")))
    implementation(project(mapOf("path" to ":APP")))
    implementation(project(mapOf("path" to ":GPS")))
    implementation(project(mapOf("path" to ":NAV")))
    implementation(project(mapOf("path" to ":GPX")))

    //기존 implementation
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material:1.5.1")
    implementation("com.google.android.datatransport:transport-runtime:3.1.9")
    implementation("androidx.wear.compose:compose-material:1.2.0")
    implementation("androidx.paging:paging-common-ktx:3.2.1")
    implementation(libs.firebase.auth.ktx)
    implementation(libs.play.services.location)
    implementation("com.google.firebase:firebase-messaging:23.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //추가 implementation
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")
    implementation("androidx.compose.ui:ui-util:1.5.1")
    implementation("androidx.compose.material3:material3:1.2.0-alpha08")
    implementation("com.google.accompanist:accompanist-pager:0.28.0") // Pager
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0") // Pager Indicators
    implementation("com.patrykandpatrick.vico:compose-m3:1.12.0")

    //카카오 SDK
    implementation("com.kakao.sdk:v2-all:2.15.0")

    //구글 SDK
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")

    //네이버 SDK
    implementation("com.navercorp.nid:oauth-jdk8:5.6.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    //kapt ("android.arch.lifecycle:compiler:1.1.1")
    //ksp ("com.github.bumptech.glide:ksp:4.16.0")
    // Skip this if you don't want to use integration libraries or configure Glide.
    implementation("com.google.dagger:hilt-android:2.47")
    //kapt("com.google.dagger:hilt-android-compiler:2.47")
}