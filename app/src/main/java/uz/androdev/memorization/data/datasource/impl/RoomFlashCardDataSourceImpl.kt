package uz.androdev.memorization.data.datasource.impl

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.data.datasource.FlashCardDataSource
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:52 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RoomFlashCardDataSourceImpl @Inject constructor(
    private val flashCardDao: FlashCardDao
) : FlashCardDataSource {
    @Throws(SQLiteConstraintException::class)
    override suspend fun createFlashCard(flashCardInput: FlashCardInput) {
        val flashCardEntity = FlashCardEntity(
            folderId = flashCardInput.folderId,
            question = flashCardInput.question,
            answer = flashCardInput.answer,
            createdAt = flashCardInput.dateTime
        )
        flashCardDao.insertFlashCard(flashCardEntity)
    }

    override fun getFlashCards(folderId: Long): Flow<List<FlashCard>> {
        return flashCardDao.getAllFlashCards(folderId).map { list ->
            list.map {
                FlashCard(
                    id = it.id,
                    question = it.question,
                    answer = it.answer
                )
            }
        }
    }

    override suspend fun updateFlashCard(flashCard: FlashCard) {
        TODO("Not yet implemented")
    }
}