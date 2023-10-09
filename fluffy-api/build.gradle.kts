import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
    id("org.liquibase.gradle") version "2.2.0"
    id("nu.studer.jooq") version "8.2"
    kotlin("jvm") version "1.8.22"
    kotlin("plugin.spring") version "1.8.22"
    id("io.gitlab.arturbosch.detekt") version("1.22.0")
}

group = "com.fluffy"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Arrow
    implementation("io.arrow-kt:arrow-core:1.0.1")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:1.3.0")

    // Liquibase
    liquibaseRuntime("org.liquibase:liquibase-core:4.20.0")
    liquibaseRuntime("org.postgresql:postgresql:42.5.4")
    liquibaseRuntime("info.picocli:picocli:4.7.3")

    // jOOQ
    jooqGenerator("org.postgresql:postgresql:42.5.4")
    implementation("org.springframework.boot:spring-boot-starter-jooq:3.1.0")
    runtimeOnly("org.postgresql:postgresql:42.5.4")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val dbHost = System.getenv("DB_MASTER_HOST") ?: "localhost"
val dbPort = System.getenv("DB_MASTER_PORT") ?: "10233"
val dbUser = System.getenv("DB_USER") ?: "root"
val dbPass = System.getenv("DB_PASS") ?: "root"
val dbName = System.getenv("DB_NAME") ?: "fluffy"

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

jooq {
    version.set("3.18.2")
    edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://$dbHost:$dbPort/$dbName"
                    user = dbUser
                    password = dbPass
                }
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                    }
                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                    target.apply {
                        packageName = "com.fluffy.fluffyapi.driver.fluffydb"
                        directory = "build/generated-src/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

detekt {
    config.setFrom(file("config/detekt/detekt.yml"))
    buildUponDefaultConfig = true
    ignoreFailures = true
}
