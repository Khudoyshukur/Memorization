package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:56 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RemoveFlashCardUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : RemoveFlashCardUseCase {
    override suspend fun invoke(flashCard: FlashCard): UseCaseResponse<Unit, UnitFailure> {
        return try {
            flashCardRepository.removeFlashCard(flashCard)
            UseCaseResponse.Success(Unit)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UnitFailure)
        }
    }
}