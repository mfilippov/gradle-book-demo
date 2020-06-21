import org.gradle.api.Task
import org.gradle.internal.impldep.org.junit.Assert
import org.gradle.internal.impldep.org.junit.Test
import org.gradle.testfixtures.ProjectBuilder

class InfoTaskTest {
    @Test
    fun createTaskInProject() {
        val newTask = createInfoTask()
        assert(newTask is InfoTask)
    }

    @Test
    fun propertyValueIsSet() {
        val newTask = createInfoTask() as InfoTask
        newTask.apply {
            prefix = "test"
        }
        Assert.assertEquals("test", newTask.prefix)
    }

    private fun createInfoTask(): Task {
        val project = ProjectBuilder.builder().build()
        return project.tasks.create("info", InfoTask::class.java)
    }
}