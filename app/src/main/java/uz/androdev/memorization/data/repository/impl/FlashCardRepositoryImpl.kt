package uz.androdev.memorization.data.repository.impl

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.data.datasource.FlashCardDataSource
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 2:57 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

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
}