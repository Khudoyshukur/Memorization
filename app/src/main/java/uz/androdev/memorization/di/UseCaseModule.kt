package uz.androdev.memorization.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.domain.usecase.GetFlashCardsUseCase
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
import uz.androdev.memorization.domain.usecase.impl.CreateFlashCardUseCaseImpl
import uz.androdev.memorization.domain.usecase.impl.CreateFolderUseCaseImpl
import uz.androdev.memorization.domain.usecase.impl.GetFlashCardsUseCaseImpl
import uz.androdev.memorization.domain.usecase.impl.GetFoldersUseCaseImpl

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 3:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun bindCreateFolderUseCase(impl: CreateFolderUseCaseImpl): CreateFolderUseCase

    @Binds
    fun bindGetFoldersUseCase(impl: GetFoldersUseCaseImpl): GetFoldersUseCase

    @Binds
    fun bindCreateFlashCardUseCase(impl: CreateFlashCardUseCaseImpl): CreateFlashCardUseCase

    @Binds
    fun bindGetFlashCardsUseCase(impl: GetFlashCardsUseCaseImpl): GetFlashCardsUseCase
}