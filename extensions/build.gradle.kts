open class ConvertTask : DefaultTask() {
    @InputFile
    var source: File? = null
    @OutputFile
    var output: File? = null

    @org.gradle.api.tasks.TaskAction
    fun convert() {
        val xml = groovy.util.XmlSlurper().parse(source)

        output?.writer().use {
            xml.childNodes().forEach { personNode ->
                if (personNode is groovy.util.slurpersupport.Node) {
                    val name = personNode.attributes()["name"]
                    val email = personNode.attributes()["email"]
                    it?.write("$name,$email \n")
                }
            }
            println("Converted ${source?.name} to ${output?.name}")
        }
    }
}

task<InfoTask>("info") {
    prefix = "Running Gradle"
}

task<ConvertTask>("convert") {
    source = file("src/people.xml")
    output = file("$buildDir/convert-output.txt")
}