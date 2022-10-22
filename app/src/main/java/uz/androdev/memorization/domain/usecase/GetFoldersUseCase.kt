package uz.androdev.memorization.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:49 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface GetFoldersUseCase {
    operator fun invoke(): Flow<List<Folder>>
}