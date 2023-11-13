// build.gradle.kts generated by Skip for SkipModel
plugins {
    kotlin("android") version "1.9.0"
    id("com.android.library") version "8.1.0"
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.robolectric:robolectric:4.10.3")
    androidTestImplementation("androidx.test:runner:1.5.2")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    testImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test:core:1.5.0")
    testImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    api(platform("androidx.compose:compose-bom:2023.09.00"))
    api("androidx.compose.runtime:runtime")
    implementation(project(":SkipLib"))
    testImplementation(project(":SkipUnit"))
    androidTestImplementation(project(":SkipUnit"))
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = group as String
    compileSdk = 34
    defaultConfig {
        minSdk = 29
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
    kotlinOptions {
        suppressWarnings = true
    }
}

tasks.withType<Test>().configureEach {
    systemProperties.put("robolectric.logging", "stdout")
    systemProperties.put("robolectric.graphicsMode", "NATIVE")
    testLogging {
        this.showStandardStreams = true
    }
}
