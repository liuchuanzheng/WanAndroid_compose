plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    compileOnly("com.android.tools.lint:lint-api:31.2.2")
    compileOnly("com.android.tools.lint:lint-checks:31.2.2")
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
}
tasks.jar {
    manifest {
        attributes["Lint-Registry-v2"] = "com.lcz.wanandroid_compose.lint.CustomLintRegistry"
    }
}