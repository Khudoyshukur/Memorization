package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.UpdateFolderUseCase
import uz.androdev.memorization.domain.usecase.UpdateFolderUseCaseFailure
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:00 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class UpdateFolderUseCaseImpl @Inject constructor(
    private val folderRepository: FolderRepository
) : UpdateFolderUseCase {
    override suspend fun invoke(folder: Folder): UseCaseResponse<Unit, UpdateFolderUseCaseFailure> {
        return try {
            folderRepository.updateFolder(folder)
            UseCaseResponse.Success(Unit)
        } catch (e: IllegalArgumentException) {
            UseCaseResponse.Failure(UpdateFolderUseCaseFailure.FolderNotFound)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UpdateFolderUseCaseFailure.UnknownError)
        }
    }
}