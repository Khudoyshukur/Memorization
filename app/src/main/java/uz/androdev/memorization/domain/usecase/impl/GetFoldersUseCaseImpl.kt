package uz.androdev.memorization.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:52 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class GetFoldersUseCaseImpl @Inject constructor(
    private val folderRepository: FolderRepository
) : GetFoldersUseCase {
    override fun invoke(): Flow<List<Folder>> {
        return folderRepository.getFolders()
    }
}