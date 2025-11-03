import org.gradle.kotlin.dsl.implementation
import org.gradle.kotlin.dsl.releaseImplementation

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
}

android {
    signingConfigs {
        create("release") {
            storeFile = file("${rootProject.projectDir}/keystore/liuchuanzheng.jks")
            storePassword = "123456"
            keyAlias = "liuchuanzheng"
            keyPassword = "123456"
        }
    }
    namespace = "com.lcz.wanandroid_compose"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lcz.wanandroid_compose"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true // 启用代码混淆
            isShrinkResources = true // 移除未使用资源
//            isDebuggable = false // 禁用调试模式
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true //开启 BuildConfig 生成
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // 使用最新稳定版
        // 添加以下配置允许实验性API
        kotlinCompilerExtensionVersion += "-experimental"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)  // 添加Coil Compose库
    implementation ("androidx.compose.material:material-icons-extended")//谷歌官方的图标扩展库。会增大apk体积。一般商业项目不用，因为图标也不符合公司ui设计
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.persistentCookieJar)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.compose.ui.text)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.constraintlayout.core)
    debugImplementation("com.github.chuckerteam.chucker:library:3.5.2")
    releaseImplementation("com.github.chuckerteam.chucker:library-no-op:3.5.2")
    //以下都是测试相关的
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}