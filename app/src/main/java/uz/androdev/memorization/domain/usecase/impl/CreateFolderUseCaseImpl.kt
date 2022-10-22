package uz.androdev.memorization.domain.usecase.impl

import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.model.input.FolderInput
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:33 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CreateFolderUseCaseImpl @Inject constructor(
    private val folderRepository: FolderRepository
) : CreateFolderUseCase {
    override suspend fun invoke(folderInput: FolderInput): UseCaseResponse<Unit, UnitFailure> {
        return try {
            folderRepository.createFolder(folderInput)
            UseCaseResponse.Success(Unit)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(UnitFailure)
        }
    }
}