package uz.androdev.memorization.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.GetPracticeFlashCardsUseCase
import uz.androdev.memorization.domain.util.ModelInput
import uz.androdev.memorization.domain.util.PracticeModel
import uz.androdev.memorization.domain.util.PracticeModelState
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 08-11-2022
 * Time: 9:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestPracticeModel {
    private lateinit var practiceModel: PracticeModel
    private lateinit var getPracticeFlashCardsUseCase: GetPracticeFlashCardsUseCase

    @Before
    fun setUp() {
        getPracticeFlashCardsUseCase = mock()
        practiceModel = PracticeModel(getPracticeFlashCardsUseCase)
    }

    @Test
    fun testInitialState() {
        assertEquals(practiceModel.state.value, PracticeModelState.Idle)
    }

    @Test
    fun start_beforeStarting_shouldTriggerLoadingState() = runTest {
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .then {
                assertEquals(practiceModel.state.value, PracticeModelState.Initializing)
                UseCaseResponse.Success<List<FlashCard>>(emptyList())
            }

        practiceModel.processInput(ModelInput.StartPractice(10L))
    }

    @Test
    fun start_whenFailure_shouldTriggerFailureState() = runTest {
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UnitFailure))

        practiceModel.processInput(ModelInput.StartPractice(10L))
        assertEquals(practiceModel.state.value, PracticeModelState.FailedToInitialize)
    }

    @Test
    fun start_whenNoFlashCards_shouldTriggerEmptyState() = runTest {
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Success(emptyList()))

        practiceModel.processInput(ModelInput.StartPractice(10L))
        assertEquals(practiceModel.state.value, PracticeModelState.EmptyFlashCards)
    }

    @Test
    fun start_whenEnoughFlashCards_shouldTriggerPracticeState() = runTest {
        val flashCards = List(5) { FlashCardFactory.createNewFlashCard() }
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Success(flashCards))

        practiceModel.processInput(ModelInput.StartPractice(10L))
        assertEquals(practiceModel.state.value, PracticeModelState.Practice(flashCards.first()))
    }

    @Test
    fun next_shouldTriggerNextPracticeState() = runTest {
        val flashCards = List(5) { FlashCardFactory.createNewFlashCard() }
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Success(flashCards))

        practiceModel.processInput(ModelInput.StartPractice(10L))
        assertEquals(practiceModel.state.value, PracticeModelState.Practice(flashCards[0]))

        practiceModel.processInput(ModelInput.Next(MemorizationLevel.LOW))
        assertEquals(practiceModel.state.value, PracticeModelState.Practice(flashCards[1]))

        practiceModel.processInput(ModelInput.Next(MemorizationLevel.LOW))
        assertEquals(practiceModel.state.value, PracticeModelState.Practice(flashCards[2]))
    }

    @Test
    fun next_whenNoFlashCards_shouldTriggerFinishedState() = runTest {
        val flashCards = List(1) { FlashCardFactory.createNewFlashCard() }
        whenever(getPracticeFlashCardsUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Success(flashCards))

        practiceModel.processInput(ModelInput.StartPractice(10L))
        assertEquals(practiceModel.state.value, PracticeModelState.Practice(flashCards[0]))

        practiceModel.processInput(ModelInput.Next(MemorizationLevel.LOW))
        assertEquals(practiceModel.state.value, PracticeModelState.Finished)
    }
}