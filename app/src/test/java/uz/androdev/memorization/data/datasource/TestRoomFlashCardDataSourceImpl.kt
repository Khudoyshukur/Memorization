package uz.androdev.memorization.data.datasource

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import uz.androdev.memorization.data.datasource.impl.RoomFlashCardDataSourceImpl
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.fake.FakeFlashCardDao
import uz.androdev.memorization.data.util.PracticeFlashCardSelector
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.factory.FolderFactory
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.input.PracticeFlashCardsFilterInput
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
    private lateinit var practiceFlashCardSelector: PracticeFlashCardSelector

    @Before
    fun setUp() {
        flashCardDao = Mockito.spy(FakeFlashCardDao())
        practiceFlashCardSelector = mock()
        dataSourceImpl = RoomFlashCardDataSourceImpl(flashCardDao, practiceFlashCardSelector)
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
        dataSourceImpl = RoomFlashCardDataSourceImpl(flashCardDao, practiceFlashCardSelector)

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
            answer = UUID.randomUUID().toString(),
            memorizationLevel = MemorizationLevel.HIGH
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

    @Test
    fun removeFlashCard_shouldDelegateToDao() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard()
        dataSourceImpl.removeFlashCard(flashCard)

        Mockito.verify(flashCardDao).removeFlashCard(eq(flashCard.id))
    }

    @Test
    fun getPracticeFlashCards_shouldDelegateToSelector() = runTest {
        val resultFlashCards = List(10) {
            FlashCardFactory.createNewFlashCard()
        }
        whenever(practiceFlashCardSelector.select(any()))
            .thenReturn(resultFlashCards)

        val input = PracticeFlashCardsFilterInput(
            folderId = 10L,
            size = 10,
            memorizationLevelPercentages = mapOf(
                MemorizationLevel.LOW to 100
            )
        )
        val flashCards = dataSourceImpl.getFlashCardsToPractice(input)

        Mockito.verify(practiceFlashCardSelector).select(eq(input))
        assertEquals(flashCards, resultFlashCards)
    }

    @Test
    fun createFlashCard_shouldCreateFlashCardWithLowMemorizationLevel() = runTest {
        val input = FlashCardFactory.createFlashCardInput()
        dataSourceImpl.createFlashCard(input)

        Mockito.verify(flashCardDao).insertFlashCard(argThat {
            memorizationLevel == MemorizationLevel.LOW
        })
    }
}