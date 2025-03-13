import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}
var localProperties = Properties()
localProperties.load(FileInputStream(rootProject.file("local.properties")))
android.buildFeatures.buildConfig = true

android {
    namespace = "com.koory1st.unionshare"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.koory1st.unionshare"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "JD_URL", "\"" + localProperties["jd.url"] + "\"")
            buildConfigField("String", "JD_APP_KEY", "\"" + localProperties["jd.appKey"] + "\"")
            buildConfigField(
                "String",
                "JD_APP_SECRET",
                "\"" + localProperties["jd.appSecret"] + "\""
            )

            buildConfigField("String", "TB_URL", "\"" + localProperties["tb.url"] + "\"")
            buildConfigField("String", "TB_APP_KEY", "\"" + localProperties["tb.appKey"] + "\"")
            buildConfigField(
                "String",
                "TB_APP_SECRET",
                "\"" + localProperties["tb.appSecret"] + "\""
            )
            buildConfigField("String", "TB_AD_ZONE", "\"" + localProperties["tb.adZone"] + "\"")
        }
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

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
        exclude("META-INF/*.kotlin_module")
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
    implementation(files("/home/levy/Downloads/open-api-sdk-2.0-2024-04-07.jar"))
    // https://mvnrepository.com/artifact/org.codehaus.jackson/jackson-mapper-asl
    implementation(libs.jackson.mapper.asl)
    implementation(libs.jackson.core.asl)
    implementation(files("/home/levy/Downloads/taobao-sdk-java-auto_1707103653430-20240318.jar"))
    //OkHttp
    implementation(libs.okhttp)
    implementation(libs.okio)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

