package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.usecase.impl.CreateFlashCardUseCaseImpl
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.factory.FolderFactory

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
}