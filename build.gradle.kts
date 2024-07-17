plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "1.9.20"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

group = "com.dchistyakov"
version = "1.0-SNAPSHOT"
description = "Personal library for my use"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.3")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            from(components["kotlin"])

            pom {
                name.set("${project.group}:${project.name}")
                description.set("Personal library for my use.")
                url.set("https://github.com/oURMIo/dach-library/")
                inceptionYear.set("2024")

                scm {
                    connection.set("scm:git:git://github.com/oURMIo/dach-library.git")
                    developerConnection.set("scm:git:git://github.com/oURMIo/dach-library.git")
                    url.set("https://github.com/oURMIo/dach-library")
                }
                developers {
                    developer {
                        name.set("Dmitry Chistyakov")
                        email.set("dimach.98.ru@gmail.com")
                        roles.set(listOf("Chief Developer"))
                    }
                }
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }
                ciManagement {
                    system.set("Github Actions")
                    url.set("https://github.com/oURMIo/dach-library/actions")
                }
            }
        }
    }

    repositories {
        maven {
            name = "ossrh"
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        }
        maven {
            name = "ossrhSnapshots"
            url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots")
        }
    }
}

signing {
    sign(publishing.publications["mavenKotlin"])
}

tasks.withType<Jar> {
    archiveClassifier.set("javadoc")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.isIncremental = true
}