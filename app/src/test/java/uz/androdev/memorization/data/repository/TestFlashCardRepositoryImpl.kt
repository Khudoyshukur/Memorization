package uz.androdev.memorization.data.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import uz.androdev.memorization.data.datasource.FlashCardDataSource
import uz.androdev.memorization.data.repository.impl.FlashCardRepositoryImpl
import uz.androdev.memorization.data.repository.impl.PRACTICE_FLASH_CARD_LIST_SIZE
import uz.androdev.memorization.data.repository.impl.PRACTICE_FLASH_CARD_MEMORIZATION_LEVEL_PERCENTAGES
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 2:59 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFlashCardRepositoryImpl {
    private lateinit var flashCardRepositoryImpl: FlashCardRepositoryImpl
    private lateinit var flashCardDataSource: FlashCardDataSource

    @Before
    fun setUp() {
        flashCardDataSource = mock()
        flashCardRepositoryImpl = FlashCardRepositoryImpl(flashCardDataSource)
    }

    @Test
    fun createFlashCard_delegatesToDataSource() = runTest {
        val input = FlashCardFactory.createFlashCardInput()
        flashCardRepositoryImpl.createFlashCard(input)

        Mockito.verify(flashCardDataSource).createFlashCard(eq(input))
    }

    @Test
    fun getFlashCards_getsValuesFromDataSource() = runTest {
        val folderId = 10L
        val flashCards = List(10) {
            FlashCardFactory.createNewFlashCard()
        }

        whenever(flashCardDataSource.getFlashCards(folderId))
            .thenReturn(flowOf(flashCards))

        val receivedFlashCards = flashCardRepositoryImpl.getFlashCards(folderId).first()

        Mockito.verify(flashCardDataSource).getFlashCards(folderId)
        assertEquals(receivedFlashCards, flashCards)
    }

    @Test
    fun updateFlashCard_shouldDelegateToDataSource() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard()
        flashCardRepositoryImpl.updateFlashCard(flashCard)

        Mockito.verify(flashCardDataSource).updateFlashCard(eq(flashCard))
    }

    @Test
    fun removeFlashCard_shouldDelegateToDataSource() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard()
        flashCardRepositoryImpl.removeFlashCard(flashCard)

        Mockito.verify(flashCardDataSource).removeFlashCard(eq(flashCard))
    }

    @Test
    fun getFlashCardsToPractice_shouldUseStaticConstants_andShouldDelegateToDataSource() = runTest {
        val resultFlashCards = List(5) {
            FlashCardFactory.createNewFlashCard()
        }
        whenever(flashCardDataSource.getFlashCardsToPractice(any()))
            .thenReturn(resultFlashCards)

        val folderId = 10L
        val flashCards = flashCardRepositoryImpl.getFlashCardsToPractice(folderId)

        assertEquals(flashCards, resultFlashCards)
        Mockito.verify(flashCardDataSource).getFlashCardsToPractice(
            argThat {
                this.folderId == folderId &&
                        this.size == PRACTICE_FLASH_CARD_LIST_SIZE &&
                        this.memorizationLevelPercentages == PRACTICE_FLASH_CARD_MEMORIZATION_LEVEL_PERCENTAGES
            }
        )
    }
}