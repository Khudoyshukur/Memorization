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
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.UpdateFlashCardUseCaseFailure
import uz.androdev.memorization.domain.usecase.impl.UpdateFlashCardUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 30-10-2022
 * Time: 12:36 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestUpdateFlashCardUseCase {
    private lateinit var flashCardRepository: FlashCardRepository
    private lateinit var updateFlashCardUseCaseImpl: UpdateFlashCardUseCaseImpl

    @Before
    fun setUp() {
        flashCardRepository = mock()
        updateFlashCardUseCaseImpl = UpdateFlashCardUseCaseImpl(flashCardRepository)
    }

    @Test
    fun invoke_shouldDelegateToRepository() = runTest {
        val flashCard = FlashCardFactory.createNewFlashCard()
        updateFlashCardUseCaseImpl.invoke(flashCard)

        Mockito.verify(flashCardRepository).updateFlashCard(eq(flashCard))
    }

    @Test
    fun whenRepositorySucceed_shouldReturnSuccess() = runTest {
        whenever(flashCardRepository.updateFlashCard(any()))
            .thenReturn(Unit)

        val resp = updateFlashCardUseCaseImpl.invoke(FlashCardFactory.createNewFlashCard())
        assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun whenRepositoryFailsWithIllegalArgumentException_shouldReturnFlashCardNotFound() = runTest {
        whenever(flashCardRepository.updateFlashCard(any()))
            .thenThrow(IllegalArgumentException())

        val resp = updateFlashCardUseCaseImpl.invoke(FlashCardFactory.createNewFlashCard())
        assertEquals(resp, UseCaseResponse.Failure(UpdateFlashCardUseCaseFailure.FlashCardNotFound))
    }

    @Test
    fun whenRepositoryFailsWithException_shouldReturnUnknownError() = runTest {
        whenever(flashCardRepository.updateFlashCard(any()))
            .thenThrow(java.lang.RuntimeException())

        val resp = updateFlashCardUseCaseImpl.invoke(FlashCardFactory.createNewFlashCard())
        assertEquals(resp, UseCaseResponse.Failure(UpdateFlashCardUseCaseFailure.UnknownErrorOccurred))
    }
}