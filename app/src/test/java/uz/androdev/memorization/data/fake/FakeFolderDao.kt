package uz.androdev.memorization.data.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.model.entity.FolderEntity
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:39 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FakeFolderDao @Inject constructor() : FolderDao {
    private var currentId = 1L
    private val folders = MutableStateFlow<List<FolderEntity>>(emptyList())

    override suspend fun insertFolder(folderEntity: FolderEntity): Long {
        val folder = folders.value.find {
            it.id == folderEntity.id
        }

        return if (folder == null) {
            folders.update {
                it + listOf(folderEntity.copy(id = currentId))
            }

            currentId++
        } else {
            folders.update {
                it.map {
                    if (it.id == folderEntity.id) {
                        folderEntity
                    } else {
                        it
                    }
                }
            }

            folderEntity.id
        }
    }

    override fun getFolders(): Flow<List<FolderEntity>> {
        return folders
    }

    override suspend fun getFolderByTitle(title: String): FolderEntity? {
        return folders.value.find { it.title == title }
    }

    override suspend fun getFolderById(id: Long): FolderEntity? {
        return folders.value.find {
            it.id == id
        }
    }

    override suspend fun removeFolder(folderId: Long) {
        folders.update {
            it.filter { it.id != folderId }
        }
    }
}