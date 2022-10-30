package uz.androdev.memorization.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import uz.androdev.memorization.data.datasource.FolderDataSource
import uz.androdev.memorization.data.repository.impl.FolderRepositoryImpl
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:15 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFolderRepository {
    private lateinit var folderRepository: FolderRepository
    private lateinit var folderDataSource: FolderDataSource
    private lateinit var testDispatcher: CoroutineDispatcher

    @Before
    fun setUp() {
        testDispatcher = UnconfinedTestDispatcher()
        folderDataSource = mock()
        folderRepository = FolderRepositoryImpl(folderDataSource, testDispatcher)
    }

    @Test
    fun createFolder_delegatesToDataSource() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()
        folderRepository.createFolder(folderInput)

        Mockito.verify(folderDataSource).createFolder(eq(folderInput))
    }

    @Test
    fun getFolders_delegatesToDataSource() = runTest {
        folderRepository.getFolders()

        Mockito.verify(folderDataSource).getFolders()
    }

    @Test
    fun updateFolder_shouldDelegateToDataSource() = runTest {
        val folder = FolderFactory.createFolder()
        folderRepository.updateFolder(folder)

        Mockito.verify(folderDataSource).updateFolder(eq(folder))
    }

    @Test
    fun removeFolder_shouldDelegateToDataSource() = runTest {
        val folder = FolderFactory.createFolder()
        folderRepository.removeFolder(folder)

        Mockito.verify(folderDataSource).removeFolder(eq(folder))
    }
}