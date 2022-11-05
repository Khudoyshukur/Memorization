package uz.androdev.memorization.data.datasource.impl

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.data.datasource.FlashCardDataSource
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.util.PracticeFlashCardSelector
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.input.PracticeFlashCardsFilterInput
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:52 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RoomFlashCardDataSourceImpl @Inject constructor(
    private val flashCardDao: FlashCardDao,
    private val practiceFlashCardSelector: PracticeFlashCardSelector
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
                    answer = it.answer,
                    memorizationLevel = it.memorizationLevel
                )
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun updateFlashCard(flashCard: FlashCard) {
        val flashCardEntity = flashCardDao.getFlashCardById(flashCard.id)
            ?: throw IllegalArgumentException("Flash Card not found!")

        flashCardDao.insertFlashCard(
            flashCardEntity.copy(
                question = flashCard.question,
                answer = flashCard.answer,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun removeFlashCard(flashCard: FlashCard) {
        flashCardDao.removeFlashCard(flashCard.id)
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun getFlashCardsToPractice(
        input: PracticeFlashCardsFilterInput
    ): List<FlashCard> {
        return practiceFlashCardSelector.select(input)
    }
}