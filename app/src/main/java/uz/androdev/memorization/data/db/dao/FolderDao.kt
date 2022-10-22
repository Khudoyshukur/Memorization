package uz.androdev.memorization.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.entity.FolderEntity

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:22 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folderEntity: FolderEntity): Long

    @Query("select * from folders order by title asc")
    fun getFolders(): Flow<List<FolderEntity>>

    @Query("select * from folders where title == :title")
    fun getFolderByTitle(title: String): FolderEntity?
}