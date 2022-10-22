package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.usecase.impl.GetFoldersUseCaseImpl
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:53 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetFoldersUseCaseImpl {
    private lateinit var folderRepository: FolderRepository
    private lateinit var getFoldersUseCaseImpl: GetFoldersUseCaseImpl

    @Before
    fun setUp() {
        folderRepository = mock()
        getFoldersUseCaseImpl = GetFoldersUseCaseImpl(folderRepository)
    }

    @Test
    fun invoke_delegatesToRepository() = runTest {
        getFoldersUseCaseImpl.invoke()

        Mockito.verify(folderRepository).getFolders()
    }

    @Test
    fun invoke_getsValuesFromRepository() = runTest {
        val folders = List(10) {
            FolderFactory.createFolder()
        }

        whenever(folderRepository.getFolders())
            .thenReturn(flowOf(folders))

        val resp = getFoldersUseCaseImpl.invoke()
        assertTrue(resp.first() == folders)
    }
}