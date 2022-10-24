package uz.androdev.memorization.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:08 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface GetFlashCardsUseCase {
    operator fun invoke(folderId: Long): Flow<List<FlashCard>>
}