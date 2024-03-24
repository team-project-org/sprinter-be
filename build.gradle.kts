import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("plugin.jpa") version "1.9.20"
    kotlin("kapt") version "1.9.20"
    id("org.flywaydb.flyway") version "7.7.3"
    id("com.ewerk.gradle.plugins.querydsl") version "1.0.10"
    id("com.netflix.dgs.codegen") version "5.2.0" apply true
}

group = "hackathon"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
}

repositories {
    mavenCentral()
}

val swaggerVersion = "3.0.0"
val queryDslVersion = "5.0.0"
val dgsVersion = "4.9.16"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.data:spring-data-envers")

    // QUERY_DSL
    implementation("com.querydsl:querydsl-jpa")
    implementation("com.querydsl:querydsl-apt")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // AWS
    implementation("com.amazonaws:aws-java-sdk:1.12.394")

    // JWT
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.2")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.2")

    // GSON
    implementation("com.google.code.gson:gson")

    // SWAGGER
    implementation("io.springfox:springfox-boot-starter:${swaggerVersion}")

    // DB, FLYWAY
    implementation("mysql:mysql-connector-java:8.0.26")

    // QueryDSL
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jpa")
    kapt("org.springframework.boot:spring-boot-configuration-processor")

    // CSV
    implementation("org.apache.commons:commons-csv:1.10.0")

    // DGS
    implementation("com.graphql-java:graphql-java-extended-scalars")
    implementation(platform("com.netflix.graphql.dgs:graphql-dgs-platform-dependencies:$dgsVersion"))
    runtimeOnly("com.netflix.graphql.dgs:graphql-dgs-spring-boot-starter:$dgsVersion")
    implementation("com.netflix.graphql.dgs:graphql-dgs-extended-scalars")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework:spring-webflux")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("org.springframework.security:spring-security-test")

    // MOCKK
    testImplementation("io.mockk:mockk:1.13.3")

    // TEST
    testImplementation("io.kotest:kotest-runner-junit5:4.6.3")
    testImplementation("io.kotest:kotest-assertions-core:4.6.3")

    // JSOUP
    implementation("org.jsoup:jsoup:1.17.2")

    //selenium
    implementation ("org.seleniumhq.selenium:selenium-java:4.6.0")
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

val querydslDir = "$buildDir/generated/querydsl"

querydsl {
    library = "com.querydsl:querydsl-apt"
    jpa = true
    querydslSourcesDir = querydslDir
}

flyway {
    val defaultUrl = "127.0.0.1"
    val defaultUser = "root"
    val defaultPassword = "sprinter"
    val prodUrl = System.getenv("DB_URL") ?: defaultUrl
    val prodUser = System.getenv("DB_USER") ?: defaultUser
    val prodPassword = System.getenv("DB_PASSWORD") ?: defaultPassword

    url = "jdbc:mysql://${prodUrl}:3306/sprinter?serverTimezone=UTC&characterEncoding=UTF-8"
    user = prodUser
    password = prodPassword
}

tasks.withType<com.netflix.graphql.dgs.codegen.gradle.GenerateJavaTask> {
    schemaPaths = mutableListOf("${projectDir}/src/main/resources/schema/schema.graphqls")
    generateDataTypes = true
    snakeCaseConstantNames = true
    language = "kotlin"
    generateKotlinNullableClasses = false
    typeMapping = mutableMapOf(
        "DateTime" to "java.time.OffsetDateTime",
        "Long" to "kotlin.Long",
        "RoleType" to "hackathon.sprinter.util.RoleType",
        "JobLevel" to "hackathon.sprinter.util.JobLevel",
        "JobGroup" to "hackathon.sprinter.util.JobGroup",
        "Job" to "hackathon.sprinter.util.Job",
        "JobSkill" to "hackathon.sprinter.util.JobSkill",
        "AffiliationType" to "hackathon.sprinter.util.AffiliationType",
    )
}
