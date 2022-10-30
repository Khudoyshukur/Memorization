package uz.androdev.memorization.data.datasource.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.data.datasource.FolderDataSource
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.model.entity.FolderEntity
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:38 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class RoomFolderDataSourceImpl @Inject constructor(
    private val folderDao: FolderDao
) : FolderDataSource {
    override suspend fun createFolder(folderInput: FolderInput) {
        var title = folderInput.title
        var appendNumber = 1
        while (folderDao.getFolderByTitle(title) != null) {
            title = "${folderInput.title}(${appendNumber++})"
        }

        val entity = FolderEntity(
            title = title,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        folderDao.insertFolder(entity)
    }

    override fun getFolders(): Flow<List<Folder>> {
        return folderDao.getFolders().map { list ->
            list.map {
                Folder(
                    id = it.id,
                    title = it.title
                )
            }
        }
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun updateFolder(folder: Folder) {
        val entityInDb = folderDao.getFolderById(folder.id)
            ?: throw IllegalArgumentException("Folder not found!")
        folderDao.insertFolder(
            entityInDb.copy(
                title = folder.title,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    @Throws(IllegalArgumentException::class)
    override suspend fun removeFolder(folder: Folder) {
        val folder = folderDao.getFolderById(folder.id)
            ?: throw IllegalArgumentException("Folder not found!")
        folderDao.removeFolder(folder.id)
    }
}