plugins {
    id("java")
}

group = "de.bethibande"
version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.palantir.javapoet:javapoet:0.7.0")

    implementation("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    implementation(projects.core)
}

tasks.test {
    useJUnitPlatform()
}