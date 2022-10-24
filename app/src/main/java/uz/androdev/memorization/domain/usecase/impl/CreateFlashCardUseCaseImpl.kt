package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.model.input.FlashCardInput
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:15 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CreateFlashCardUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : CreateFlashCardUseCase {
    override suspend fun invoke(flashCardInput: FlashCardInput) {
        flashCardRepository.createFlashCard(flashCardInput)
    }
}