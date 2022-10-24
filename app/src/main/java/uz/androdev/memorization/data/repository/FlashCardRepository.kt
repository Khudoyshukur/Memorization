package uz.androdev.memorization.data.repository

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 2:57 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface FlashCardRepository {
    @Throws(SQLiteConstraintException::class)
    suspend fun createFlashCard(flashCardInput: FlashCardInput)

    fun getFlashCards(folderId: Long): Flow<List<FlashCard>>
}