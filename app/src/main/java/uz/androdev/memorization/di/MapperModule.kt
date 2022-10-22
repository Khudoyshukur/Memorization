package uz.androdev.memorization.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.model.entity.FolderEntity
import uz.androdev.memorization.model.mapper.impl.FolderEntityToFolderMapper
import uz.androdev.memorization.model.mapper.Mapper
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:55 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface MapperModule {

    @Binds
    fun bindFolderEntityToFolderMapper(
        mapper: FolderEntityToFolderMapper
    ): Mapper<FolderEntity, Folder>
}