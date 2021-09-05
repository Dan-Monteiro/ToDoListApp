package brcom.dan.example.todolist.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import brcom.dan.example.todolist.databinding.ActivityDetailsBinding
import brcom.dan.example.todolist.extensions.text
import brcom.dan.example.todolist.model.Task

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startListners()

        val task: Task? = intent.getParcelableExtra<Task>(TASK_DETAILED)
        populateFields(task)

    }

    private fun startListners(){

        setSupportActionBar(binding.mtbAddTask)

        binding.mtbAddTask.setNavigationOnClickListener {
            finish()
        }
    }

    private fun populateFields(task: Task?){
        task?.let {
            binding.tilTitulo.text = task.title
            binding.tilDate.text = task.date
            binding.tilHour.text = task.hour
            binding.tilObservation.text = task.observation
        }
    }

    companion object {
       const val TASK_DETAILED = "task"
    }
}