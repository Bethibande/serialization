plugins {
    id("java")
    `java-library`
    `maven-publish`
    signing
}

description = "Annotation processor for the serialization library"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("com.palantir.javapoet:javapoet:0.7.0")

    compileOnly("org.projectlombok:lombok:1.18.38")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")

    implementation("com.google.auto.service:auto-service:1.1.1")
    annotationProcessor("com.google.auto.service:auto-service:1.1.1")

    testCompileOnly("org.jetbrains:annotations:26.0.2")

    implementation(projects.core)
    testAnnotationProcessor(projects.processor)
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