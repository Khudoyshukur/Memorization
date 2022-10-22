package uz.androdev.memorization.domain.usecase

import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.model.input.FolderInput

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:27 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface CreateFolderUseCase {
    suspend operator fun invoke(folderInput: FolderInput): UseCaseResponse<Unit, UnitFailure>
}


