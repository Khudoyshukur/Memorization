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
import uz.androdev.memorization.domain.usecase.RemoveFlashCardUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:57 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestRemoveFlashCardUseCaseImpl {
    private lateinit var flashCardRepository: FlashCardRepository
    private lateinit var removeFlashCardUseCaseImpl: RemoveFlashCardUseCaseImpl

    @Before
    fun setUp() {
        flashCardRepository = mock()
        removeFlashCardUseCaseImpl = RemoveFlashCardUseCaseImpl(flashCardRepository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard()
        removeFlashCardUseCaseImpl.invoke(flashCard)

        Mockito.verify(flashCardRepository).removeFlashCard(eq(flashCard))
    }

    @Test
    fun whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(flashCardRepository.removeFlashCard(any()))
            .thenReturn(Unit)

        val resp = removeFlashCardUseCaseImpl(FlashCardFactory.createNewFlashCard())
        assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun whenRepositoryFailed_shouldReturnFailure() = runTest {
        whenever(flashCardRepository.removeFlashCard(any()))
            .thenThrow(RuntimeException())

        val resp = removeFlashCardUseCaseImpl(FlashCardFactory.createNewFlashCard())
        assertEquals(resp, UseCaseResponse.Failure(UnitFailure))
    }
}