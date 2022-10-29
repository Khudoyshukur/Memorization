package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:55 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface RemoveFlashCardUseCase {
    suspend operator fun invoke(flashCard: FlashCard): UseCaseResponse<Unit, UnitFailure>
}