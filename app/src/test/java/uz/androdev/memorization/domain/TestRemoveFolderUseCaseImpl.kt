package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.RemoveFolderUseCaseFailure
import uz.androdev.memorization.domain.usecase.impl.RemoveFolderUseCaseImpl
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:12 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestRemoveFolderUseCaseImpl {
    private lateinit var folderRepository: FolderRepository
    private lateinit var removeFolderUseCaseImpl: RemoveFolderUseCaseImpl

    @Before
    fun setUp() {
        folderRepository = mock()
        removeFolderUseCaseImpl = RemoveFolderUseCaseImpl(folderRepository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val folder = FolderFactory.createFolder()
        removeFolderUseCaseImpl.invoke(folder)

        Mockito.verify(folderRepository).removeFolder(eq(folder))
    }

    @Test
    fun whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(folderRepository.removeFolder(any()))
            .thenReturn(Unit)

        val resp = removeFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        Assert.assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun whenRepositoryReturnsIllegalArgumentException_shouldReturnNotFoundFailure() = runTest {
        whenever(folderRepository.removeFolder(any()))
            .thenThrow(IllegalArgumentException())

        val resp = removeFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        Assert.assertEquals(
            resp,
            UseCaseResponse.Failure(RemoveFolderUseCaseFailure.FolderNotFound)
        )
    }

    @Test
    fun whenRepositoryReturnsException_shouldReturnUnknownErrorFailure() = runTest {
        whenever(folderRepository.removeFolder(any()))
            .thenThrow(RuntimeException())

        val resp = removeFolderUseCaseImpl.invoke(FolderFactory.createFolder())
        Assert.assertEquals(resp, UseCaseResponse.Failure(RemoveFolderUseCaseFailure.UnknownError))
    }
}