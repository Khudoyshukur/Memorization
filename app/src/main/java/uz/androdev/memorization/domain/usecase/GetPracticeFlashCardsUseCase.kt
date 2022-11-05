package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 11:57 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface GetPracticeFlashCardsUseCase {
    suspend operator fun invoke(folderId: Long): UseCaseResponse<List<FlashCard>, UnitFailure>
}