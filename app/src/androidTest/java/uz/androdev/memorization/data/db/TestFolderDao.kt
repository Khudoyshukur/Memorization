package uz.androdev.memorization.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.factory.FolderEntityFactory
import uz.androdev.memorization.model.entity.FolderEntity
import java.io.IOException
import java.util.*

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 6:22 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class TestFolderDao {
    private lateinit var folderDao: FolderDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        folderDao = appDatabase.folderDao
    }

    @After
    @Throws(IOException::class)
    fun cleanUp() {
        appDatabase.close()
    }

    @Test
    fun insertFolder_insertsNewFolder() = runTest {
        val folder = FolderEntityFactory.createFolderEntityWithoutId()
        val id = folderDao.insertFolder(folder)

        val currentFolders = folderDao.getFolders().first()
        assertTrue(
            currentFolders.contains(
                folder.copy(id = id)
            )
        )
    }

    @Test
    fun insertFolder_whenInsertingMultipleFoldersWithTheSameTitle_shouldInsertOnlyOneOfThem(
    ) = runTest {
        val folder = FolderEntityFactory.createFolderEntityWithoutId()
        folderDao.insertFolder(folder)
        folderDao.insertFolder(folder)

        folderDao.getFolders().first().let {
            assertTrue(it.count { it.title == folder.title } == 1)
        }
    }

    @Test
    fun getFolders_returnsInsertedFolders() = runTest {
        val folders = mutableListOf<FolderEntity>()

        repeat(20) {
            folders.add(FolderEntityFactory.createFolderEntityWithoutId())
        }

        folders.forEach {
            folderDao.insertFolder(it)
        }

        val currentFolders = folderDao.getFolders().first()
        folders.forEach { folder ->
            assertTrue(currentFolders.any { it.title == folder.title })
        }
    }

    @Test
    fun insertFolder_whenExistedFolderInserted_shouldUpdateFolder() = runTest {
        val folder = FolderEntityFactory.createFolderEntityWithoutId()
        val id = folderDao.insertFolder(folder)

        val folderToUpdate = folder.copy(id = id, title = UUID.randomUUID().toString())
        val newId = folderDao.insertFolder(folderToUpdate)

        assertTrue(newId == id)

        val currentFolders = folderDao.getFolders().first()
        assertTrue(currentFolders.contains(folderToUpdate.copy(id = newId)))
        assertFalse(currentFolders.contains(folder.copy(id = newId)))
    }

    @Test
    fun getFolders_shouldReturnFoldersAlphabetically() = runTest {
        val folders = mutableListOf<FolderEntity>()
        val alphabet = arrayOf('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j')
        alphabet.shuffle()
        repeat(5) {
            val folder =
                FolderEntityFactory.createFolderEntityWithoutIdAndWith(alphabet[it].toString())
            val id = folderDao.insertFolder(folder)
            folders.add(folder.copy(id = id))
        }

        val currentFolders = folderDao.getFolders().first()

        assertTrue(currentFolders == folders.sortedBy { it.title })
    }

    @Test
    fun getFolderByTitle_returnsAppropriateFolder() = runTest {
        val folder = FolderEntityFactory.createFolderEntityWithoutId()
        val id = folderDao.insertFolder(folder)
        val insertedFolder = folder.copy(id = id)

        assertTrue(insertedFolder == folderDao.getFolderByTitle(insertedFolder.title))
    }

    @Test
    fun getFolderById_returnsCorrectFolder() = runTest {
        val folder = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            this.copy(
                id = folderDao.insertFolder(this)
            )
        }

        assertEquals(folderDao.getFolderById(folder.id), folder)
    }

    @Test
    fun getFolderById_whenNonExistentFolderRequested_shouldReturnNull() = runTest {
        assertEquals(null, folderDao.getFolderById(-10L))
    }
}