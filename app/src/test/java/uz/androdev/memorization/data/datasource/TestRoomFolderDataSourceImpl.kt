package uz.androdev.memorization.data.datasource

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import uz.androdev.memorization.data.datasource.impl.RoomFolderDataSourceImpl
import uz.androdev.memorization.data.fake.FakeFolderDao
import uz.androdev.memorization.factory.FolderFactory

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:13 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestRoomFolderDataSourceImpl {

    private lateinit var localFolderDataSourceImpl: RoomFolderDataSourceImpl
    private lateinit var fakeFolderDao: FakeFolderDao

    @Before
    fun setUp() {
        fakeFolderDao = Mockito.spy(FakeFolderDao())
        localFolderDataSourceImpl = RoomFolderDataSourceImpl(fakeFolderDao)
    }

    @Test
    fun getFolders_delegatesToFoldersDao() = runTest {
        localFolderDataSourceImpl.getFolders()

        Mockito.verify(fakeFolderDao).getFolders()
    }

    @Test
    fun createFolder_delegatesToFoldersDao() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()

        assertTrue(fakeFolderDao.getFolders().first().none { it.title == folderInput.title })
        localFolderDataSourceImpl.createFolder(folderInput)
        assertTrue(fakeFolderDao.getFolders().first().any { it.title == folderInput.title })
    }

    @Test
    fun createFolder_createsNewFolder_andReturnsCreatedFolder() = runTest {
        val newFolder = FolderFactory.createUniqueFolderInput()
        localFolderDataSourceImpl.createFolder(newFolder)

        val allFolders = localFolderDataSourceImpl.getFolders().first()
        assertTrue(allFolders.size == 1)
        assertTrue(allFolders.any { it.title == newFolder.title })
    }

    @Test
    fun createFolder_whenFolderAlreadyExist_appendsOrderOfFolder() = runTest {
        val newFolder = FolderFactory.createUniqueFolderInput()
        localFolderDataSourceImpl.createFolder(newFolder)
        localFolderDataSourceImpl.createFolder(newFolder)
        localFolderDataSourceImpl.createFolder(newFolder)

        val allFolders = localFolderDataSourceImpl.getFolders().first()

        assertTrue(allFolders.size == 3)
        assertTrue(allFolders.any { it.title == newFolder.title })
        assertTrue(allFolders.any { it.title == "${newFolder.title}(1)" })
        assertTrue(allFolders.any { it.title == "${newFolder.title}(2)" })
    }
}