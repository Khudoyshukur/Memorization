package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.impl.CreateFolderUseCaseImpl
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:26 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestCreateFolderUseCaseImpl {
    private lateinit var folderRepository: FolderRepository
    private lateinit var createFolderUseCase: CreateFolderUseCaseImpl

    @Before
    fun setUp() {
        folderRepository = mock()
        createFolderUseCase = CreateFolderUseCaseImpl(folderRepository)
    }

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()
        createFolderUseCase.invoke(folderInput)

        Mockito.verify(folderRepository).createFolder(eq(folderInput))
    }

    @Test
    fun invoke_whenRepositoryInvokesNormally_shouldReturnSuccess() = runTest {
        whenever(folderRepository.createFolder(any()))
            .thenReturn(Unit)

        val resp = createFolderUseCase.invoke(FolderFactory.createUniqueFolderInput())
        assertTrue(resp is UseCaseResponse.Success)
    }

    @Test
    fun invoke_whenRepositoryThrowsException_returnsFailure() = runTest {
        whenever(folderRepository.createFolder(any()))
            .thenThrow(RuntimeException())

        val resp = createFolderUseCase.invoke(FolderFactory.createUniqueFolderInput())
        assertTrue(resp is UseCaseResponse.Failure)
    }
}