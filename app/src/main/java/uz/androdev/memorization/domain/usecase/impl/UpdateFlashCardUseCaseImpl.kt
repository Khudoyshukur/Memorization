package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.UpdateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.UpdateFlashCardUseCaseFailure
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:35 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class UpdateFlashCardUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : UpdateFlashCardUseCase {
    override suspend fun invoke(flashCard: FlashCard): UseCaseResponse<Unit, UpdateFlashCardUseCaseFailure> {
        return try {
            flashCardRepository.updateFlashCard(flashCard)
            UseCaseResponse.Success(Unit)
        } catch (e: IllegalArgumentException) {
            UseCaseResponse.Failure(UpdateFlashCardUseCaseFailure.FlashCardNotFound)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UpdateFlashCardUseCaseFailure.UnknownErrorOccurred)
        }
    }
}