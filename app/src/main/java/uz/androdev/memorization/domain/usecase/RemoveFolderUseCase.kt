package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UseCaseFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:10 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface RemoveFolderUseCase {
    suspend operator fun invoke(folder: Folder): UseCaseResponse<Unit, RemoveFolderUseCaseFailure>
}

sealed interface RemoveFolderUseCaseFailure : UseCaseFailure {
    object FolderNotFound : RemoveFolderUseCaseFailure
    object UnknownError : RemoveFolderUseCaseFailure
}