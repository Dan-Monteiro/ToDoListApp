package brcom.dan.example.todolist.ui

import androidx.lifecycle.*
import brcom.dan.example.todolist.datasource.TaskDao
import brcom.dan.example.todolist.model.Task
import kotlinx.coroutines.launch

class MainViewModel(private val taskDao: TaskDao): ViewModel() {

    val allTasks: LiveData<List<Task>> = taskDao.getList().asLiveData()

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }
}

class MainViewModelFactory(private val taskDao: TaskDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
