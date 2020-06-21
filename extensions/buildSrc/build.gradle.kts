plugins {
    `kotlin-dsl`
}

repositories {
    jcenter()
}

val finishMessage: Task by tasks.creating {
    doLast{
        println("Done building buildSrc")
    }
}

tasks.findByName("build")?.finalizedBy(finishMessage)

dependencies {
    testImplementation("junit:junit:4.12")
}