plugins {
    id("java")
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "seminars"
version = "1.0-SNAPSHOT"


//tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
//    mainClass = "Main"
//}
//
//tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
//    mainClass = "Main"
//}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "Main"
    }
}