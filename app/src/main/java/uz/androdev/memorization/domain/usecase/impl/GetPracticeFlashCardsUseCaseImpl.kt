package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.GetPracticeFlashCardsUseCase
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 11:59 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetPracticeFlashCardsUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : GetPracticeFlashCardsUseCase {
    override suspend fun invoke(folderId: Long): UseCaseResponse<List<FlashCard>, UnitFailure> {
        return try {
            val flashCards = flashCardRepository.getFlashCardsToPractice(folderId)
            UseCaseResponse.Success(flashCards)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UnitFailure)
        }
    }
}