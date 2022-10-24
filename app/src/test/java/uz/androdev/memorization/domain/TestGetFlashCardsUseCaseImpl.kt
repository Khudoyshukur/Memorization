package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.usecase.impl.GetFlashCardsUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:10 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetFlashCardsUseCaseImpl {
    private lateinit var getFlashCardsUseCaseImpl: GetFlashCardsUseCaseImpl
    private lateinit var flashCardRepository: FlashCardRepository

    @Before
    fun setUp() {
        flashCardRepository = mock()
        getFlashCardsUseCaseImpl = GetFlashCardsUseCaseImpl(flashCardRepository)
    }

    @Test
    fun invoke_getsFlashCardsFromRepository() = runTest {
        val folderId = 10L
        val flashCards = List(10) {
            FlashCardFactory.createNewFlashCard()
        }

        whenever(flashCardRepository.getFlashCards(folderId))
            .thenReturn(flowOf(flashCards))

        val receivedFlashCards = getFlashCardsUseCaseImpl(folderId).first()

        Mockito.verify(flashCardRepository).getFlashCards(folderId)
        Assert.assertEquals(receivedFlashCards, flashCards)
    }
}