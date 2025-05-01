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

    implementation(projects.core)
    implementation(projects.serialNetty)
    annotationProcessor(projects.processor)

    implementation("io.netty:netty-buffer:4.2.0.Final")

    implementation("org.openjdk.jmh:jmh-core:1.37")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.test {
    useJUnitPlatform()
}