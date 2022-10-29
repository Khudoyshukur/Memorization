package uz.androdev.memorization.data.db.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.entity.FlashCardEntity

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 10:59 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Dao
interface FlashCardDao {

    @Insert
    @Throws(SQLiteConstraintException::class)
    suspend fun insertFlashCard(flashCardEntity: FlashCardEntity): Long

    @Query("select * from flash_cards where folder_id == :folderId")
    fun getAllFlashCards(folderId: Long): Flow<List<FlashCardEntity>>

    @Query("select * from flash_cards where id=:id")
    suspend fun getFlashCardById(id: Long): FlashCardEntity?

    @Query("delete from flash_cards where id=:id")
    suspend fun removeFlashCard(id: Long)
}