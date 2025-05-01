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
    implementation("io.netty:netty-buffer:4.2.0.Final")
}

tasks.test {
    useJUnitPlatform()
}