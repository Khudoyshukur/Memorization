package uz.androdev.memorization.data.repository.impl

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.data.datasource.FlashCardDataSource
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.input.PracticeFlashCardsFilterInput
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 2:57 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

const val PRACTICE_FLASH_CARD_LIST_SIZE = 10
val PRACTICE_FLASH_CARD_MEMORIZATION_LEVEL_PERCENTAGES = mapOf(
    MemorizationLevel.LOW to 70, // percent
    MemorizationLevel.MEDIUM to 20, // percent
    MemorizationLevel.HIGH to 10, // percent
)

class FlashCardRepositoryImpl @Inject constructor(
    private val flashCardDataSource: FlashCardDataSource
) : FlashCardRepository {
    override suspend fun createFlashCard(flashCardInput: FlashCardInput) {
        flashCardDataSource.createFlashCard(flashCardInput)
    }

    override fun getFlashCards(folderId: Long): Flow<List<FlashCard>> {
        return flashCardDataSource.getFlashCards(folderId)
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun updateFlashCard(flashCard: FlashCard) {
        flashCardDataSource.updateFlashCard(flashCard)
    }

    override suspend fun removeFlashCard(flashCard: FlashCard) {
        flashCardDataSource.removeFlashCard(flashCard)
    }

    override suspend fun getFlashCardsToPractice(folderId: Long): List<FlashCard> {
        return flashCardDataSource.getFlashCardsToPractice(
            input = PracticeFlashCardsFilterInput(
                folderId = folderId,
                size = PRACTICE_FLASH_CARD_LIST_SIZE,
                memorizationLevelPercentages = PRACTICE_FLASH_CARD_MEMORIZATION_LEVEL_PERCENTAGES
            )
        )
    }
}