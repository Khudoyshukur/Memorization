package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.UpdateFolderUseCase
import uz.androdev.memorization.domain.usecase.UpdateFolderUseCaseFailure
import uz.androdev.memorization.domain.usecase.impl.UpdateFolderUseCaseImpl
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:02 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestUpdateFolderUseCaseImpl {
    private lateinit var folderRepository: FolderRepository
    private lateinit var updateFolderUseCaseImpl: UpdateFolderUseCaseImpl

    @Before
    fun setUp() {
        folderRepository = mock()
        updateFolderUseCaseImpl = UpdateFolderUseCaseImpl(folderRepository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val folder = FolderFactory.createFolder()
        updateFolderUseCaseImpl.invoke(folder)

        Mockito.verify(folderRepository).updateFolder(eq(folder))
    }

    @Test
    fun whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(folderRepository.updateFolder(any()))
            .thenReturn(Unit)

        val resp = updateFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun whenRepositoryReturnsIllegalArgumentException_shouldReturnNotFoundFailure() = runTest {
        whenever(folderRepository.updateFolder(any()))
            .thenThrow(IllegalArgumentException())

        val resp = updateFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        assertEquals(resp, UseCaseResponse.Failure(UpdateFolderUseCaseFailure.FolderNotFound))
    }

    @Test
    fun whenRepositoryReturnsException_shouldReturnUnknownErrorFailure() = runTest {
        whenever(folderRepository.updateFolder(any()))
            .thenThrow(RuntimeException())

        val resp = updateFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        assertEquals(resp, UseCaseResponse.Failure(UpdateFolderUseCaseFailure.UnknownError))
    }
}