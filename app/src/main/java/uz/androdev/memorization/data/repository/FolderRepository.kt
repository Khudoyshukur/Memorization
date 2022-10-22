package uz.androdev.memorization.data.repository

import kotlinx.coroutines.flow.Flow
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:16 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface FolderRepository {
    suspend fun createFolder(folderInput: FolderInput)
    fun getFolders(): Flow<List<Folder>>
}