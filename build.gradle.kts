defaultTasks("second", "convert")

project.description = "Simple project"
project.version = "1.0"

val dir = project.file(file("config"), PathValidation.DIRECTORY)

task("first") {
    description = "My first task"
    group = "base"
    doLast {
        println("Run ${this.name}")
    }
}

val delegatedTask: Task by tasks.creating {
    val who by extra("who")
    doLast {
        println("Hello $who")
    }
}

val skipped: Task by tasks.creating {
    onlyIf {
        false
    }
    doLast {
        println("Skipped")
    }
}

val skippedByException: Task by tasks.creating {
    doFirst {
        throw StopExecutionException()
    }
}

val disabledTask: Task by tasks.creating {
    val dir = File("assemble")
    enabled = dir.exists()

    doLast {
        println("Run ${this.name}")
    }
}

delegatedTask.extra["who"] = "world"

task("second") {
    description = "My second task"
    group = "base"
    dependsOn("first", delegatedTask, skipped, disabledTask, skippedByException)
    doLast {
        println("Run ${this.name}")
    }
}

tasks.addRule("Pattern: ping<ID>") {
    val taskName = this
    if (taskName.startsWith("ping")) {
        task(taskName) {
            doLast {
                println("Pinging ${taskName.replace("ping", "")}")
            }
        }
    }
}

task("convert") {
    val source = file("source.xml")
    val output = file("output.txt")

    inputs.file(source)
    outputs.files(output)

    doLast {
        val xml = groovy.util.XmlSlurper().parse(source)

        output.writer().use {
            xml.childNodes().forEach { personNode ->
                if (personNode is groovy.util.slurpersupport.Node) {
                    val name = personNode.attributes()["name"]
                    val email = personNode.attributes()["email"]
                    it.write("$name,$email \n")
                }
            }
        }
        println("Converted ${source.name} to ${output.name}")
    }
}

val taskOutputFiles = files(tasks.named("convert"))

task("createVersionDir") {
    val outputDir = file("output")

    inputs.property("version", project.version)

    outputs.dir(outputDir)

    doLast {
        println("Making directory ${outputDir.name}")
        mkdir(outputDir)
    }
}

task("convertFiles") {
    inputs.files("input/input1.xml", "input/input2.xml")

    outputs.upToDateWhen {
        File("output").listFiles()?.any { "output.*\\.xml".toRegex().matches(it.name) } ?: false
    }

    doLast {
        println("Running convertFiles")
    }
}

task("treeVisitor") {
    val projectFiles = fileTree(".")
    doLast {
        projectFiles.visit(object: FileVisitor {
            override fun visitFile(fileDetails: FileVisitDetails) {
                println("File: ${fileDetails.path}, size: ${fileDetails.size}")
            }

            override fun visitDir(dirDetails: FileVisitDetails) {
                println("Directory: ${dirDetails.path}")
            }

        })
    }
}