package brcom.dan.example.todolist.datasource

import androidx.room.*
import brcom.dan.example.todolist.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM Task WHERE id = :taskId")
    suspend fun findById(taskId: Int): Task?

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}