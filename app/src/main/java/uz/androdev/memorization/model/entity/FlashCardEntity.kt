package uz.androdev.memorization.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.model.enums.MemorizationLevel

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 10:14 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Entity(
    tableName = "flash_cards",
    foreignKeys = [
        ForeignKey(
            entity = FolderEntity::class,
            parentColumns = ["id"],
            childColumns = ["folder_id"]
        )
    ],
    indices = [
        Index("folder_id")
    ]
)
data class FlashCardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "folder_id") val folderId: Long,
    @ColumnInfo(name = "question") val question: String,
    @ColumnInfo(name = "answer") val answer: String,
    @ColumnInfo(name = "created_at") val createdAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "updated_at") val updatedAt: LocalDateTime = LocalDateTime.now(),
    @ColumnInfo(name = "memorization_level") val memorizationLevel: MemorizationLevel = MemorizationLevel.LOW,
    @ColumnInfo(name = "repetition_count") val repetitionCount: Long = 0
)