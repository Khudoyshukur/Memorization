package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.impl.GetPracticeFlashCardsUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 12:02 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestGetPracticeFlashCardsUseCaseImpl {
    private lateinit var flashCardRepository: FlashCardRepository
    private lateinit var getPracticeFlashCardsUseCaseImpl: GetPracticeFlashCardsUseCaseImpl

    @Before
    fun setUp() {
        flashCardRepository = mock()
        getPracticeFlashCardsUseCaseImpl = GetPracticeFlashCardsUseCaseImpl(flashCardRepository)
    }

    @Test
    fun shouldDelegateToRepository() = runTest {
        val resultFlashCards = List(5) {
            FlashCardFactory.createNewFlashCard()
        }
        whenever(flashCardRepository.getFlashCardsToPractice(any()))
            .thenReturn(resultFlashCards)

        val folderId = 10L
        val resp = getPracticeFlashCardsUseCaseImpl.invoke(folderId)

        if (resp !is UseCaseResponse.Success) {
            throw java.lang.AssertionError("Assertion failed!")
        }
        assertEquals(resp.data, resultFlashCards)
        Mockito.verify(flashCardRepository).getFlashCardsToPractice(eq(folderId))
    }

    @Test
    fun whenRepositoryInvokesWithFailure_shouldReturnFailure() = runTest {
        whenever(flashCardRepository.getFlashCardsToPractice(any()))
            .thenThrow(java.lang.RuntimeException::class.java)

        val resp = getPracticeFlashCardsUseCaseImpl.invoke(10L)

        assertEquals(resp, UseCaseResponse.Failure(UnitFailure))
    }
}