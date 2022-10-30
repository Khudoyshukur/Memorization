package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UseCaseFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 11:59 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface UpdateFolderUseCase {
    suspend operator fun invoke(folder: Folder): UseCaseResponse<Unit, UpdateFolderUseCaseFailure>
}

sealed interface UpdateFolderUseCaseFailure : UseCaseFailure {
    object FolderNotFound : UpdateFolderUseCaseFailure
    object UnknownError : UpdateFolderUseCaseFailure
}