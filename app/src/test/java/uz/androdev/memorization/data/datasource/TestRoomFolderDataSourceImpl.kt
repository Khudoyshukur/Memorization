package uz.androdev.memorization.data.datasource

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq
import uz.androdev.memorization.data.datasource.impl.RoomFolderDataSourceImpl
import uz.androdev.memorization.data.fake.FakeFolderDao
import uz.androdev.memorization.factory.FolderFactory
import java.util.UUID

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

    @Test
    fun updateFolder_shouldUpdateFolder_andDelegateToDao() = runTest {
        val input = FolderFactory.createUniqueFolderInput()
        localFolderDataSourceImpl.createFolder(input)

        val insertedFolder = localFolderDataSourceImpl.getFolders().first().first()

        // make sure input is written to a database
        assertTrue(insertedFolder.title == input.title)

        val folderToUpdate = insertedFolder.copy(title = UUID.randomUUID().toString())
        localFolderDataSourceImpl.updateFolder(folderToUpdate)

        val updatedFolder = localFolderDataSourceImpl.getFolders().first().first()
        assertTrue(updatedFolder == folderToUpdate)
    }

    @Test
    fun updateFolder_shouldUpdateUpdateTime_andShouldNotUpdateCreateTime() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()
        localFolderDataSourceImpl.createFolder(folderInput)

        val currentEntityInDao = fakeFolderDao.getFolders().first().find {
            it.title == folderInput.title
        }!!

        // record times
        val createdAtTime = currentEntityInDao.createdAt
        val updatedAtTime = currentEntityInDao.updatedAt

        // update folder
        val folder = localFolderDataSourceImpl.getFolders().first().find {
            it.title == folderInput.title
        }!!
        localFolderDataSourceImpl.updateFolder(folder.copy(title = UUID.randomUUID().toString()))

        val updatedEntityInDao = fakeFolderDao.getFolders().first().find {
            it.id == currentEntityInDao.id
        }!!

        assertEquals(createdAtTime, updatedEntityInDao.createdAt)
        assertNotEquals(updatedAtTime, updatedEntityInDao.updatedAt)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateFolder_whenNotExistentFolderIsUpdated_shouldThrowException() = runTest {
        val folder = FolderFactory.createFolder()

        localFolderDataSourceImpl.updateFolder(folder)
    }


    @Test
    fun removeFolder_shouldDelegateToDao() = runTest {
        localFolderDataSourceImpl.createFolder(FolderFactory.createUniqueFolderInput())

        val folder = localFolderDataSourceImpl.getFolders().first().first()
        localFolderDataSourceImpl.removeFolder(folder)

        Mockito.verify(fakeFolderDao).removeFolder(eq(folder.id))
    }

    @Test(expected = IllegalArgumentException::class)
    fun removeFolder_whenNonExistentFolderRequested_shouldThrowIllegalArgumentException() =
        runTest {
            localFolderDataSourceImpl.removeFolder(
                FolderFactory.createFolder().copy(id = 10L)
            )
        }
}