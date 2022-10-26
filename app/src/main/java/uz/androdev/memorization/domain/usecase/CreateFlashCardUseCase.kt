package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UseCaseFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.input.FlashCardInput

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:14 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface CreateFlashCardUseCase {
    suspend operator fun invoke(flashCardInput: FlashCardInput): UseCaseResponse<Unit, CreateFlashCardUseCaseFailure>
}

sealed interface CreateFlashCardUseCaseFailure : UseCaseFailure {
    object CouldNotCreateFlashCard : CreateFlashCardUseCaseFailure
    object FolderDoesNotExist : CreateFlashCardUseCaseFailure
}