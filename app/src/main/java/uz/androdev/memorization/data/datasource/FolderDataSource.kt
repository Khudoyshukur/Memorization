package uz.androdev.memorization.data.datasource

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:32 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface FolderDataSource {
    suspend fun createFolder(folderInput: FolderInput)
    suspend fun updateFolder(folder: Folder)
    fun getFolders(): Flow<List<Folder>>
}