package uz.androdev.memorization.model.mapper.impl

import uz.androdev.memorization.model.entity.FolderEntity
import uz.androdev.memorization.model.mapper.Mapper
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:56 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FolderEntityToFolderMapper @Inject constructor(
) : Mapper<FolderEntity, Folder> {
    override fun map(source: FolderEntity): Folder {
        return Folder(
            id = source.id,
            title = source.title
        )
    }
}