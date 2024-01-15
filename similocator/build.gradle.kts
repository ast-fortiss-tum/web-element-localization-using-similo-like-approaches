plugins {
    id("java")
}

group = "de.tum.ls4"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.seleniumhq.selenium:selenium-java:4.14.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.7.1")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.2.0")
    implementation("com.typesafe:config:1.4.3")
    implementation("org.apache.commons:commons-text:1.11.0")
}



tasks.test {
    useJUnitPlatform()
}
