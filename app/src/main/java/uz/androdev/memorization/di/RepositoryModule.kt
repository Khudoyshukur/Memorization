package uz.androdev.memorization.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.data.repository.impl.FlashCardRepositoryImpl
import uz.androdev.memorization.data.repository.impl.FolderRepositoryImpl

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 3:36 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface FolderRepositoryModule {
    @Binds
    fun bindFolderRepository(impl: FolderRepositoryImpl): FolderRepository
}

@Module
@InstallIn(SingletonComponent::class)
interface FlashCardRepositoryModule {
    @Binds
    fun bindFlashCardRepository(impl: FlashCardRepositoryImpl): FlashCardRepository
}