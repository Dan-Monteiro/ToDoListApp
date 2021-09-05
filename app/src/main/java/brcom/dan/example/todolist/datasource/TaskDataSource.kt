package brcom.dan.example.todolist.datasource

import brcom.dan.example.todolist.model.Task

object TaskDataSource {

    private val list = arrayListOf<Task>()

    fun getList() = list.toList()

    fun insertTask(task: Task) {
        list.add(task.copy(id = list.size + 1))
    }

    fun findById(taskId: Int): Task? {
        return list.find {
            it.id == taskId
        }
    }

    fun updateTask(task: Task) {
        list.forEachIndexed { index, _task ->
            if(_task.id == task.id) {
                list[index] = task
            }
        }
    }

    fun deleteTask(task: Task) {
        list.remove(task)
    }

}