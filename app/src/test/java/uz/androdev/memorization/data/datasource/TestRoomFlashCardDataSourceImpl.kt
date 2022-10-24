package uz.androdev.memorization.data.datasource

import android.database.sqlite.SQLiteConstraintException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.datasource.impl.RoomFlashCardDataSourceImpl
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.data.fake.FakeFlashCardDao
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.factory.FolderFactory

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
        val savedFlashCards = List(10) {
            FlashCardFactory.createNewFlashCardEntity(folderId = folder.id).also {
                flashCardDao.insertFlashCard(it)
            }
        }

        val flashCardsFromDataSource = dataSourceImpl.getFlashCards(folder.id).first()

        assertEquals(
            savedFlashCards.map { it.id },
            flashCardsFromDataSource.map { it.id }
        )
    }
}