import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("application")
    id("org.springframework.boot") version "3.0.6"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.openapi.generator") version "6.5.0"
    kotlin("jvm") version "1.7.22"
    kotlin("plugin.spring") version "1.7.22"
}

group = "com.fluffy"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.swagger.core.v3:swagger-annotations:2.2.8")
    compileOnly("io.swagger.core.v3:swagger-models:2.2.8")
    compileOnly("jakarta.annotation:jakarta.annotation-api:2.1.1")
    compileOnly("jakarta.annotation:jakarta.annotation-api:2.1.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("jakarta.servlet:jakarta.servlet-api:4.0.4")
    implementation("jakarta.validation:jakarta.validation-api:2.0.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("com.mysql:mysql-connector-j")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$projectDir/_build/openapi.yaml")
    outputDir.set("$buildDir/openapi/server-code/")
    apiPackage.set("com.fluffy.openapi.generated.controller")
    modelPackage.set("com.fluffy.openapi.generated.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
        )
    )
    additionalProperties.set(
        mapOf(
            "useTags" to "true"
        )
    )
}

tasks.register("bundleSwagger", Exec::class) {
    group = "swagger"
    description = "Bundle Swagger/OpenAPI file using swagger-cli."

    val inputSpecPath = "src/main/resources/openapi.yaml"
    val outputSpecPath = "_build/openapi.yaml"

    commandLine("swagger-cli", "bundle", inputSpecPath, "--outfile", outputSpecPath, "--type", "yaml")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.compileKotlin {
    dependsOn("openApiGenerate")
}

tasks.openApiGenerate {
    dependsOn("bundleSwagger")
}

kotlin.sourceSets.main {
    kotlin.srcDir("$buildDir/openapi/server-code/src/main")
}
