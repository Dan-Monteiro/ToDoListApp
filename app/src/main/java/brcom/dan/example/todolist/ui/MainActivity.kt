package brcom.dan.example.todolist.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.room.Room
import brcom.dan.example.todolist.App
import brcom.dan.example.todolist.databinding.ActivityMainBinding
import brcom.dan.example.todolist.datasource.AppDatabase
import brcom.dan.example.todolist.model.Task
import brcom.dan.example.todolist.utils.GlobalIdentifiers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: AppDatabase
    private val adapter by lazy {  TaskListAdapter() }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).database.taskDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTaskList.adapter = adapter

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            GlobalIdentifiers.DB_NAME
        ).build()

        mainViewModel.allTasks.observe(this, Observer { tasks ->
            tasks?.let {
                adapter.submitList(tasks)
                shouldShowEmptyState(tasks.isEmpty())
            }
        })

        startListeners()
    }

    private fun startListeners() {
        binding.fbAdd.setOnClickListener {
            startActivityForResult(Intent(this, AddTaskActivity::class.java), CREATE_NEW_TASK)
        }

        adapter.listenerEdit = {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra(AddTaskActivity.TASK, it)
            startActivityForResult( intent, UPDATE_TASK)
        }

        adapter.listenerDelete = {
            GlobalScope.launch {
                mainViewModel.deleteTask(it)
            }
        }

        adapter.listenerDetails = {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.TASK_DETAILED, it)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK){
            if (data != null) {
                val task: Task? = data.extras?.getParcelable<Task>(AddTaskActivity.EXTRA_INSERT)
                if (task != null) {
                    mainViewModel.insertTask(task)
                    Toast.makeText(this, "Tarefa criada com sucesso!", Toast.LENGTH_LONG).show()
                }
            }
        }else if(requestCode == UPDATE_TASK && resultCode == Activity.RESULT_OK){
            if (data != null) {
                val task: Task? = data.extras?.getParcelable<Task>(AddTaskActivity.EXTRA_UPDATE)
                if (task != null) {
                    mainViewModel.updateTask(task)
                    Toast.makeText(this, "Tarefa atualizada com sucesso!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun shouldShowEmptyState(shouldShow: Boolean){
        binding.include.emptyState.visibility = if(!shouldShow) View.GONE else View.VISIBLE
    }

    companion object {
        private const val CREATE_NEW_TASK = 1
        private const val UPDATE_TASK = 2
    }
}