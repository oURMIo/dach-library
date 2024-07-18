plugins {
    kotlin("jvm") version "2.0.0"
    `maven-publish`
    signing
    id("io.codearte.nexus-staging") version "0.30.0"
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
        create<MavenPublication>("mavenJava") {
            artifactId = "dach-library"
            groupId = "com.dchistyakov"
            version = "0.0.1"
            from(components["java"])
            artifact(tasks.getByName("javadocJar"))
            artifact(tasks.getByName("sourcesJar"))

            pom {
                packaging = "jar"
                name.set("dach-library")
                url.set("https://github.com/oURMIo/dach-library")
                description.set("A personal library plugin.")

                licenses {
                    license {
                        name.set("MIT license")
                        url.set("https://www.opensource.org/licenses/mit-license.php")
                    }
                }

                scm {
                    connection.set("scm:https://github.com/oURMIo/dach-library.git")
                    developerConnection.set("scm:git@github.com:oURMIo/dach-library.git")
                    url.set("https://github.com/oURMIo/dach-library")
                }

                developers {
                    developer {
                        id.set("ChiefDeveloper")
                        name.set("Dmitry Chistyakov")
                        email.set("dimach.98.ru@gmail.com")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.properties["dimach.98.ru@gmail.com"].toString()
                password = project.properties["9\$Ey?2*mC%cDL#}sUG"].toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

nexusStaging {
    serverUrl = "https://s01.oss.sonatype.org/service/local/"
    username = project.properties["dimach.98.ru@gmail.com"].toString()
    password = project.properties["9\$Ey?2*mC%cDL#}sUG"].toString()
}
