package uz.androdev.memorization.data.db

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.factory.FlashCardEntityFactory
import uz.androdev.memorization.factory.FolderEntityFactory
import uz.androdev.memorization.model.entity.FlashCardEntity

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:04 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFlashCardDao {
    private lateinit var flashCardDao: FlashCardDao
    private lateinit var folderDao: FolderDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        flashCardDao = appDatabase.flashCardDao
        folderDao = appDatabase.folderDao
    }

    @After
    fun cleanUp() {
        appDatabase.close()
    }

    @Test
    fun insertFlashCard_whenFolderExists_shouldInsertNewFlashCard() = runTest {
        val newFolder = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            val insertedFolderId = folderDao.insertFolder(this)
            this.copy(id = insertedFolderId)
        }

        val newFlashCard = with(
            FlashCardEntityFactory.createNewFlashCard(folderId = newFolder.id)
        ) {
            val insertedFlashCardId = flashCardDao.insertFlashCard(this)
            this.copy(id = insertedFlashCardId)
        }

        val flashCards = flashCardDao.getAllFlashCards(newFolder.id).first()
        assertTrue(flashCards.contains(newFlashCard))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertFlashCard_whenFolderNotExists_shouldThrowException() = runTest {
        val flashCard = FlashCardEntityFactory.createNewFlashCard(folderId = 5)
        flashCardDao.insertFlashCard(flashCard)
    }

    @Test
    fun getFlashCards_shouldReturnFolderSpecificFlashCards() = runTest {
        val newFolder1 = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            val insertedFolderId = folderDao.insertFolder(this)
            this.copy(id = insertedFolderId)
        }

        val newFolder2 = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            val insertedFolderId = folderDao.insertFolder(this)
            this.copy(id = insertedFolderId)
        }

        val newFlashCards = arrayListOf<FlashCardEntity>()

        newFlashCards.addAll(
            List(10) {
                with(FlashCardEntityFactory.createNewFlashCard(folderId = newFolder1.id)) {
                    val insertedFlashCardId = flashCardDao.insertFlashCard(this)
                    this.copy(id = insertedFlashCardId)
                }
            }
        )

        newFlashCards.addAll(
            List(10) {
                with(FlashCardEntityFactory.createNewFlashCard(folderId = newFolder2.id)) {
                    val insertedFlashCardId = flashCardDao.insertFlashCard(this)
                    this.copy(id = insertedFlashCardId)
                }
            }
        )


        assertEquals(
            newFlashCards.filter { it.folderId == newFolder1.id },
            flashCardDao.getAllFlashCards(newFolder1.id).first()
        )

        assertEquals(
            newFlashCards.filter { it.folderId == newFolder2.id },
            flashCardDao.getAllFlashCards(newFolder2.id).first()
        )
    }

    @Test
    fun getFlashCardById_shouldReturnCorrectFlashCard() = runTest {
        val folder = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            this.copy(
                id = folderDao.insertFolder(this)
            )
        }
        val flashCardEntity =
            with(FlashCardEntityFactory.createNewFlashCard(folderId = folder.id)) {
                this.copy(
                    id = flashCardDao.insertFlashCard(this)
                )
            }

        val response = flashCardDao.getFlashCardById(flashCardEntity.id)

        assertEquals(response, flashCardEntity)
    }

    @Test
    fun getFlashCardById_whenNonExistentFlashCardRequested_shouldReturnNull() = runTest {
        assertEquals(null, flashCardDao.getFlashCardById(-10L))
    }

    @Test
    fun deleteFlashCard_shouldDeleteFlashCard() = runTest {
        val folder = with(FolderEntityFactory.createFolderEntityWithoutId()) {
            this.copy(
                id = folderDao.insertFolder(this)
            )
        }
        val flashCardEntities = List(7) {
            with(FlashCardEntityFactory.createNewFlashCard(folderId = folder.id)) {
                this.copy(
                    id = flashCardDao.insertFlashCard(this)
                )
            }
        }
        flashCardEntities.forEach {
            assertNotNull(flashCardDao.getFlashCardById(it.id))
        }

        val flashCardEntityToRemove = flashCardEntities.random()
        flashCardDao.removeFlashCard(flashCardEntityToRemove.id)

        assertNull(flashCardDao.getFlashCardById(flashCardEntityToRemove.id))
        flashCardEntities.forEach {
            if (it.id != flashCardEntityToRemove.id) {
                assertNotNull(flashCardDao.getFlashCardById(it.id))
            }
        }
    }
}