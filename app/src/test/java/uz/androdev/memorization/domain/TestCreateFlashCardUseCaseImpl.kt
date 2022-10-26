package uz.androdev.memorization.domain

import android.database.sqlite.SQLiteConstraintException
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
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCaseFailure
import uz.androdev.memorization.domain.usecase.impl.CreateFlashCardUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:16 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestCreateFlashCardUseCaseImpl {
    private lateinit var flashCardRepository: FlashCardRepository
    private lateinit var createFlashCardUseCaseImpl: CreateFlashCardUseCaseImpl

    @Before
    fun setUp() {
        flashCardRepository = mock()
        createFlashCardUseCaseImpl = CreateFlashCardUseCaseImpl(flashCardRepository)
    }

    @Test
    fun invoke_delegatesToRepository() = runTest {
        val input = FlashCardFactory.createFlashCardInput()
        createFlashCardUseCaseImpl(input)

        Mockito.verify(flashCardRepository).createFlashCard(eq(input))
    }

    @Test
    fun invoke_shouldReturnSuccess_whenRepositoryInvokesWithoutError() = runTest {
        whenever(flashCardRepository.createFlashCard(any()))
            .thenReturn(Unit)

        val resp = createFlashCardUseCaseImpl.invoke(FlashCardFactory.createFlashCardInput())

        assertEquals(resp, UseCaseResponse.Success(Unit))
    }

    @Test
    fun invoke_shouldReturnFolderNotExists_whenRepositoryReturnsSqlConstraintException() = runTest {
        whenever(flashCardRepository.createFlashCard(any()))
            .thenThrow(SQLiteConstraintException::class.java)

        val resp = createFlashCardUseCaseImpl.invoke(FlashCardFactory.createFlashCardInput())

        assertEquals(
            resp,
            UseCaseResponse.Failure(CreateFlashCardUseCaseFailure.FolderDoesNotExist)
        )
    }


    @Test
    fun invoke_shouldReturnCannotCreateFolder_whenRepositoryReturnsException() = runTest {
        whenever(flashCardRepository.createFlashCard(any()))
            .thenThrow(RuntimeException())

        val resp = createFlashCardUseCaseImpl.invoke(FlashCardFactory.createFlashCardInput())

        assertEquals(
            resp,
            UseCaseResponse.Failure(CreateFlashCardUseCaseFailure.CouldNotCreateFlashCard)
        )
    }
}