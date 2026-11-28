import java.net.URI

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    jacoco
}

val releaseApi = providers.gradleProperty("martiniApiUrl").orElse("").get()
val debugApi = providers.gradleProperty("martiniDebugApiUrl")
    .orElse(if (releaseApi.isBlank()) "http://10.0.2.2:8080/" else releaseApi).get()
fun literalUrl(url: String) = "\"" + url.replace("\\", "\\\\").replace("\"", "\\\"") + "\""
val keyFile = rootProject.file("firma/martini-academico.jks")
val academicPassword = providers.environmentVariable("MARTINI_KEY_PASSWORD").orNull
android {
    namespace = "com.example.proyectomartini"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.proyectomartini"
        minSdk = 24
        targetSdk = 37
        versionCode = 7
        versionName = "1.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (keyFile.exists() && academicPassword != null) {
            create("academic") {
                storeFile = keyFile
                storePassword = academicPassword
                keyAlias = "martini"
                this.keyPassword = academicPassword
            }
        }
    }
    buildTypes {
        debug {
            enableUnitTestCoverage = true
            buildConfigField("String", "MARTINI_API_URL", literalUrl(debugApi))
        }
        release {
            buildConfigField("String", "MARTINI_API_URL", literalUrl(releaseApi))
            signingConfigs.findByName("academic")?.let { signingConfig = it }
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

tasks.matching { it.name == "generateReleaseBuildConfig" }.configureEach {
    doFirst {
        val uri = runCatching { URI(releaseApi) }.getOrNull()
        val host = uri?.host.orEmpty().lowercase()
        require(uri?.scheme == "https" && uri.rawPath == "/" && uri.userInfo == null &&
            uri.query == null && uri.fragment == null &&
            host.matches(Regex("[a-z0-9.-]+\\.[a-z][a-z0-9-]*")) &&
            !host.endsWith(".localhost") && !host.endsWith(".local") && !host.endsWith(".invalid")
        ) { "Genera el APK con generar-apk-aws.ps1 y el dominio público HTTPS de Master Martini." }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.4")
    testImplementation("io.mockk:mockk:1.14.5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation("androidx.navigation:navigation-compose:2.10.2")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    ksp(libs.androidx.room.compiler)
}
