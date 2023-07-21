import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("org.liquibase.gradle") version "2.2.0"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
}

group = "com.fluffy"
version = "0.0.1-SNAPSHOT"

liquibase {
    activities.register("main") {
        this.arguments = mapOf(
            "logLevel" to "info",
            "changelogFile" to "/src/main/resources/liquibase/changelog.xml",
            "url" to "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            "username" to dbUser,
            "password" to dbPass
        )
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    // Liquibase
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.postgresql:postgresql:42.5.4")
    liquibaseRuntime("info.picocli:picocli:4.7.3")
}

val dbHost = System.getenv("DB_MASTER_HOST") ?: "localhost"
val dbPort = System.getenv("DB_MASTER_PORT") ?: "15432"
val dbUser = System.getenv("DB_USER") ?: "root"
val dbPass = System.getenv("DB_PASS") ?: "root"
val dbName = System.getenv("DB_NAME") ?: "fluffy"

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
