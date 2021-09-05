package brcom.dan.example.todolist.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Task (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var title: String,
    @ColumnInfo var date: String,
    @ColumnInfo var hour: String
) : Parcelable