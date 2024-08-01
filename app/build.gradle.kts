plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.guru2.todolist"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.guru2.todolist"
        minSdk = 26
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(platform("io.github.jan-tennert.supabase:bom:2.5.4"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt:2.5.4")
    implementation("io.github.jan-tennert.supabase:gotrue-kt")
    implementation("io.github.jan-tennert.supabase:realtime-kt")

    implementation("io.ktor:ktor-client-android:2.3.12") // Android 엔진
    implementation("io.ktor:ktor-client-cio:2.3.12") // CIO 엔진
    implementation("io.ktor:ktor-client-serialization:2.3.12") // 직렬화 플러그인
    implementation("io.ktor:ktor-client-logging:2.3.12") // 로깅 플러그인
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12") // 콘텐츠 협상 플러그인

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

}