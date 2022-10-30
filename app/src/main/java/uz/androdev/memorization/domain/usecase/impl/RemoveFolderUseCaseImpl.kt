package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.RemoveFolderUseCase
import uz.androdev.memorization.domain.usecase.RemoveFolderUseCaseFailure
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:11 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RemoveFolderUseCaseImpl @Inject constructor(
    private val folderRepository: FolderRepository
) : RemoveFolderUseCase {
    override suspend fun invoke(folder: Folder): UseCaseResponse<Unit, RemoveFolderUseCaseFailure> {
        return try {
            folderRepository.removeFolder(folder)
            UseCaseResponse.Success(Unit)
        } catch (e: IllegalArgumentException) {
            UseCaseResponse.Failure(RemoveFolderUseCaseFailure.FolderNotFound)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(RemoveFolderUseCaseFailure.UnknownError)
        }
    }
}