plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.4"
    jacoco
}

group = "seminars"
version = "1.0.0"

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Core & Web
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web") // Для REST API

    // Swagger / OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")

    // Spring Boot AOP (для Decorator / Aspect)
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // БД и JPA
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Flyway для миграций БД (в Spring Boot 3.x нужен отдельный модуль для Postgres)
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // Testcontainers для интеграционных тестов
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    // gRPC Client
    implementation("net.devh:grpc-client-spring-boot-starter:3.0.0.RELEASE")
    implementation("io.grpc:grpc-protobuf:1.62.2")
    implementation("io.grpc:grpc-stub:1.62.2")
    compileOnly("org.apache.tomcat:annotations-api:6.0.53")

    implementation("org.springframework.kafka:spring-kafka")

    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.retry:spring-retry") // Для автосоздания DLQ топиков
    implementation("org.springframework.boot:spring-boot-starter-aop") // Зависимость для корректной работы @RetryableTopic
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:3.25.1" }
    plugins { create("grpc") { artifact = "io.grpc:protoc-gen-grpc-java:1.62.2" } }
    generateProtoTasks { all().forEach { task -> task.plugins { create("grpc") } } }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/java", "build/generated/source/proto/main/grpc")
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// JaCoCo конфигурация
tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

jacoco {
    toolVersion = "0.8.12"
}

springBoot {
    mainClass.set("seminars.Main")
}

