project.description = "Simple project"

tasks.register("simple") {
    doLast {
        println("Running simple task for project " + project.description)
    }
}