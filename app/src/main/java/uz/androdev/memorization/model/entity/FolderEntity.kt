package uz.androdev.memorization.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:29 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(
    tableName = "folders",
    indices = [
        Index("title", unique = true)
    ]
)
data class FolderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime = LocalDateTime.now()
)