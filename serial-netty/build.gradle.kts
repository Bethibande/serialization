plugins {
    id("java")
    `java-library`
    `maven-publish`
    signing
}

description = "Netty based reader and writer implementation for the serialization library"

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

java {
    withSourcesJar()
    withJavadocJar()
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name = project.name
                description = project.description

                url = "https://github.com/Bethibande/serialization"

                licenses {
                    license {
                        name = "GPL-3.0"
                        url = "https://raw.githubusercontent.com/Bethibande/serialization/refs/heads/master/LICENSE"
                    }
                }

                developers {
                    developer {
                        id = "bethibande"
                        name = "Max Bethmann"
                        email = "bethibande@gmail.com"
                    }
                }

                scm {
                    connection = "scm:git:git://github.com/Bethibande/serialization.git"
                    developerConnection = "scm:git:ssh://github.com/Bethibande/serialization.git"
                    url = "https://github.com/Bethibande/serialization"
                }
            }
        }
    }

    repositories {
        maven {
            name = "Maven-Releases"
            url = uri("https://pckg.bethibande.com/repository/maven-snapshots/")
            credentials {
                if (providers.gradleProperty("mavenUsername").isPresent) {
                    username = providers.gradleProperty("mavenUsername").get()
                    password = providers.gradleProperty("mavenPassword").get()
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["maven"])
}