package uz.androdev.memorization.data.datasource

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:40 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface FlashCardDataSource {
    @Throws(SQLiteConstraintException::class)
    suspend fun createFlashCard(flashCardInput: FlashCardInput)

    fun getFlashCards(folderId: Long): Flow<List<FlashCard>>

    @Throws(IllegalArgumentException::class)
    suspend fun updateFlashCard(flashCard: FlashCard)
}