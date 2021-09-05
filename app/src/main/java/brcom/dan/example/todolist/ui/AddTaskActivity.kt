package brcom.dan.example.todolist.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import brcom.dan.example.todolist.R
import brcom.dan.example.todolist.databinding.ActivityAddTaskBinding
import brcom.dan.example.todolist.datasource.AppDatabase
import brcom.dan.example.todolist.extensions.format
import brcom.dan.example.todolist.extensions.text
import brcom.dan.example.todolist.model.Task
import brcom.dan.example.todolist.utils.GlobalIdentifiers
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startListeners()

        val task: Task? = intent.getParcelableExtra(TASK)
        task?.let {
            task_id = task.id
            binding.tilTitulo.text = it.title
            binding.tilDate.text = it.date
            binding.tilHour.text = it.hour
            setUpdateLabel(it.id)
            isUpdate = true
        }
    }

    private fun startListeners() {

        setSupportActionBar(binding.mtbAddTask)

        binding.mtbAddTask.setNavigationOnClickListener {
            finish()
        }

        val tilDate = binding.tilDate
        tilDate.editText?.setOnClickListener{
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offSet = timeZone.getOffset(Date().time) * -1
                tilDate.text = Date(it + offSet).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        val tilHour = binding.tilHour
        tilHour.editText?.setOnClickListener {
            val picker =
                MaterialTimePicker
                    .Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Hour Selection")
                    .build()
            picker.show(supportFragmentManager, "TIME_PICKER_TAG")
            picker.addOnPositiveButtonClickListener{
                val minute = if(picker.minute in 0..9) "0${picker.minute}" else picker.minute.toString()
                val hour = if(picker.hour in 0..9) "0${picker.hour}" else picker.hour.toString()
                tilHour.text = "${hour}:${minute}"
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {

            val task = Task(
                id = task_id,
                title = binding.tilTitulo.text,
                date = binding.tilDate.text,
                hour = binding.tilHour.text
            )

            val replyIntent = Intent()

            if(!isUpdate){
                replyIntent.putExtra(EXTRA_INSERT, task)
                setResult(Activity.RESULT_OK, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_UPDATE, task)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }
    }

    private fun setUpdateLabel(taskId: Int) {
        if(taskId !=0){
            val updateString = resources.getString(R.string.lbl_update_task)
            binding.mtbAddTask.title = updateString
            binding.btnSave.text = updateString
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        resetUpdate()
    }

    private fun resetUpdate(){
        task_id = 0
        isUpdate = false
    }

    companion object {
        private var task_id = 0
        const val TASK = "task_obj"
        private var isUpdate = false
        const val EXTRA_INSERT = "${GlobalIdentifiers.PACKAGE_NAME}INSERT"
        const val EXTRA_UPDATE = "${GlobalIdentifiers.PACKAGE_NAME}UPDATE"
    }
}