package brcom.dan.example.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import brcom.dan.example.todolist.R
import brcom.dan.example.todolist.databinding.ItemLayoutBinding
import brcom.dan.example.todolist.model.Task

class TaskListAdapter: ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Task) {
            binding.tvTitle.text = item.title
            val displayInfo = "${item.date} ${item.hour}"
            binding.tvDateTime.text = displayInfo
            binding.imgOptions.setOnClickListener {
                showPopUp(item)
            }
        }

        private fun showPopUp(item: Task) {
            val imgOptions = binding.imgOptions
            val popupMenu = PopupMenu(imgOptions.context, imgOptions)
            popupMenu.menuInflater.inflate(R.menu.popup_task_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.action_edit -> {
                        listenerEdit(item)
                    }
                    R.id.action_delete -> {
                        listenerDelete(item)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}

class DiffCallback: DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

}