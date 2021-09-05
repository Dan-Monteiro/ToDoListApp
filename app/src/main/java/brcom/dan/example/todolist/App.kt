package brcom.dan.example.todolist

import android.app.Application
import androidx.room.Room
import brcom.dan.example.todolist.datasource.AppDatabase
import brcom.dan.example.todolist.utils.GlobalIdentifiers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class App: Application() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            GlobalIdentifiers.DB_NAME
        ).build()
    }
}