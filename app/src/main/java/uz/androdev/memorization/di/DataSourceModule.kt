package uz.androdev.memorization.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.data.datasource.FolderDataSource
import uz.androdev.memorization.data.datasource.impl.RoomFolderDataSourceImpl

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 3:37 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface FolderDataSourceModule {
    @Binds
    fun bindFolderDataSource(impl: RoomFolderDataSourceImpl): FolderDataSource
}