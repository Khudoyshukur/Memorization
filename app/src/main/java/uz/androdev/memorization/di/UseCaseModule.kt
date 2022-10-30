package uz.androdev.memorization.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import uz.androdev.memorization.domain.usecase.*
import uz.androdev.memorization.domain.usecase.impl.*

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

    @Binds
    fun bindUpdateFlashCardUseCase(impl: UpdateFlashCardUseCaseImpl): UpdateFlashCardUseCase

    @Binds
    fun bindRemoveFlashCardUseCase(impl: RemoveFlashCardUseCaseImpl): RemoveFlashCardUseCase

    @Binds
    fun bindUpdateFolderUseCase(impl: UpdateFolderUseCaseImpl): UpdateFolderUseCase
}