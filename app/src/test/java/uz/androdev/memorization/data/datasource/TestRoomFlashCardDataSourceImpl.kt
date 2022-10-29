package uz.androdev.memorization.data.datasource

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.datasource.impl.RoomFlashCardDataSourceImpl
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.fake.FakeFlashCardDao
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.factory.FolderFactory
import uz.androdev.memorization.model.model.FlashCard
import java.util.UUID

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 12:14 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestRoomFlashCardDataSourceImpl {

    private lateinit var flashCardDao: FlashCardDao
    private lateinit var dataSourceImpl: RoomFlashCardDataSourceImpl

    @Before
    fun setUp() {
        flashCardDao = Mockito.spy(FakeFlashCardDao())
        dataSourceImpl = RoomFlashCardDataSourceImpl(flashCardDao)
    }

    @Test
    fun createFlashCard_createsItemInDao() = runTest {
        val input = FlashCardFactory.createFlashCardInput()
        dataSourceImpl.createFlashCard(input)

        val flashCards = flashCardDao.getAllFlashCards(input.folderId).first()
        assertTrue(
            flashCards.count {
                it.folderId == input.folderId &&
                        it.question == input.question &&
                        it.createdAt == input.dateTime &&
                        it.answer == input.answer
            } == 1
        )
    }

    @Test(expected = SQLiteConstraintException::class)
    fun createFlashCard_whenExceptionThrows_shouldThrowThatException() = runTest {
        flashCardDao = mock()
        dataSourceImpl = RoomFlashCardDataSourceImpl(flashCardDao)

        whenever(flashCardDao.insertFlashCard(any()))
            .thenThrow(SQLiteConstraintException())

        val input = FlashCardFactory.createFlashCardInput()
        dataSourceImpl.createFlashCard(input)
    }

    @Test
    fun getFlashCards_getsFlashCardsFromDao() = runTest {
        val folder = FolderFactory.createFolder()
        val savedFlashCardIds = List(10) {
            FlashCardFactory.createNewFlashCardEntity(folderId = folder.id)
        }.map {
            flashCardDao.insertFlashCard(it)
        }

        val flashCardsFromDataSource = dataSourceImpl.getFlashCards(folder.id).first()

        assertEquals(
            savedFlashCardIds,
            flashCardsFromDataSource.map { it.id }
        )
    }

    @Test
    fun updateFlashCard_shouldUpdateFlashCard_andDelegateToDao() = runTest {
        val flashCardInput = FlashCardFactory.createFlashCardInput()
        dataSourceImpl.createFlashCard(flashCardInput)

        val insertedFlashCardEntity = flashCardDao.getAllFlashCards(flashCardInput.folderId).first()
            .find { it.answer == flashCardInput.answer && it.question == flashCardInput.question }!!
        val insertedFlashCard = dataSourceImpl.getFlashCards(flashCardInput.folderId).first()
            .find { it.answer == flashCardInput.answer && it.question == flashCardInput.question }!!

        val flashCardToUpdate = insertedFlashCard.copy(
            answer = UUID.randomUUID().toString(),
            question = UUID.randomUUID().toString()
        )

        dataSourceImpl.updateFlashCard(flashCardToUpdate)

        val currentFlashCardEntities =
            flashCardDao.getAllFlashCards(flashCardInput.folderId).first()

        assertTrue(
            currentFlashCardEntities.any {
                it.answer == flashCardToUpdate.answer && it.question == flashCardToUpdate.question
            }
        )

        assertTrue(
            currentFlashCardEntities.none {
                it.answer == insertedFlashCard.answer &&
                        it.question == insertedFlashCard.question
            }
        )

        Mockito.verify(flashCardDao).insertFlashCard(
            argThat {
                this.id == insertedFlashCardEntity.id
            }
        )
    }

    @Test
    fun updateFlashCard_shouldUpdateUpdateTime_shouldNotUpdateCreateTime() = runTest {
        val flashCardInput = FlashCardFactory.createFlashCardInput()
        dataSourceImpl.createFlashCard(flashCardInput)

        val insertedFlashCardEntity = flashCardDao.getAllFlashCards(flashCardInput.folderId)
            .first().find {
                it.question == flashCardInput.question && it.answer == flashCardInput.answer
            }!!

        val flashCardToUpdate = FlashCard(
            id = insertedFlashCardEntity.id,
            question = UUID.randomUUID().toString(),
            answer = UUID.randomUUID().toString()
        )

        dataSourceImpl.updateFlashCard(flashCardToUpdate)

        val updatedFlashCardEntity = flashCardDao.getAllFlashCards(flashCardInput.folderId)
            .first().find {
                it.question == flashCardToUpdate.question && it.answer == flashCardToUpdate.answer
            }!!

        assertEquals(insertedFlashCardEntity.createdAt, updatedFlashCardEntity.createdAt)
        assertNotEquals(insertedFlashCardEntity.updatedAt, updatedFlashCardEntity.updatedAt)
    }

    @Test(expected = IllegalArgumentException::class)
    fun updateFlashCard_whenNonExistentFlashCardRequested_shouldThrowException() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard().copy(
            id = -10L
        )
        dataSourceImpl.updateFlashCard(flashCard)
    }
}