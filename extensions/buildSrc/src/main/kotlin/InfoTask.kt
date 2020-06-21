import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

open class InfoTask : DefaultTask() {
    @Optional
    @Input
    var prefix = "Current Gradle version"

    @TaskAction
    fun info() {
        println("$prefix: ${project.gradle.gradleVersion}")
    }
}
