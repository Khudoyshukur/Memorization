package uz.androdev.memorization.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.usecase.GetFlashCardsUseCase
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:09 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetFlashCardsUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : GetFlashCardsUseCase {
    override fun invoke(folderId: Long): Flow<List<FlashCard>> {
        return flashCardRepository.getFlashCards(folderId)
    }
}