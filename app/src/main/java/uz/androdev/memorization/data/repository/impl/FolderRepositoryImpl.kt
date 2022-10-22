package uz.androdev.memorization.data.repository.impl

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.data.datasource.FolderDataSource
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:17 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FolderRepositoryImpl @Inject constructor(
    private val folderDataSource: FolderDataSource
) : FolderRepository {
    override suspend fun createFolder(folderInput: FolderInput) {
        folderDataSource.createFolder(folderInput)
    }

    override fun getFolders(): Flow<List<Folder>> {
        return folderDataSource.getFolders()
    }
}