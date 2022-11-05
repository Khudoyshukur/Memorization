package uz.androdev.memorization.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.github.javafaker.Faker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.*
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.*
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.navigation.Arguments

/**
 * Created by: androdev
 * Date: 26-10-2022
 * Time: 10:40 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFlashCardsScreenViewModel {
    private lateinit var getFlashCardsUseCase: GetFlashCardsUseCase
    private lateinit var createFlashCardUseCase: CreateFlashCardUseCase
    private lateinit var updateFlashCardUseCase: UpdateFlashCardUseCase
    private lateinit var removeFlashCardUseCase: RemoveFlashCardUseCase
    private lateinit var viewModel: FlashCardsScreenViewModel
    private lateinit var savedStateHandle: SavedStateHandle
    private val folderId = 10L
    private val testDispatcher = UnconfinedTestDispatcher()
    private val faker = Faker()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getFlashCardsUseCase = mock()
        createFlashCardUseCase = mock()
        updateFlashCardUseCase = mock()
        removeFlashCardUseCase = mock()
        savedStateHandle = mock()

        whenever(getFlashCardsUseCase.invoke(any()))
            .thenReturn(flowOf(emptyList()))

        whenever(savedStateHandle.get<Long>(Arguments.ARGUMENT_FOLDER_ID))
            .thenReturn(folderId)

        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase,
            createFlashCardUseCase,
            removeFlashCardUseCase,
            updateFlashCardUseCase,
            savedStateHandle
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        // this should trigger loading state in viewModel
        whenever(getFlashCardsUseCase(any()))
            .thenReturn(
                flow {
                    delay(1000)
                    emit(emptyList())
                }
            )
        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase,
            createFlashCardUseCase,
            removeFlashCardUseCase,
            updateFlashCardUseCase,
            savedStateHandle
        )

        val state = viewModel.uiState.value

        assertEquals(state.flashCards, null)
        assertEquals(state.flashCardScreenError, null)
        assertEquals(state.progressState.creatingFlashCard, false)
    }

    @Test
    fun uiState_shouldGetFlashCardsFromUseCase() = runTest {
        val flashCards = List(10) {
            FlashCardFactory.createNewFlashCard()
        }

        val getFlashCardsUseCase: GetFlashCardsUseCase = mock()
        whenever(getFlashCardsUseCase(any()))
            .thenReturn(flowOf(flashCards))
        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase,
            createFlashCardUseCase,
            removeFlashCardUseCase,
            updateFlashCardUseCase,
            savedStateHandle
        )

        invokeCollectingUiState {
            Mockito.verify(getFlashCardsUseCase).invoke(eq(folderId))
            val uiFlashCards = viewModel.uiState.value.flashCards
            assertEquals(uiFlashCards, flashCards)
        }
    }

    @Test
    fun createFlashCard_shouldDelegateToUseCase() = runTest {
        val action = FlashCardScreenAction.CreateFlashCard(
            question = faker.lorem().characters(),
            answer = faker.lorem().characters()
        )
        viewModel.processAction(action)

        Mockito.verify(createFlashCardUseCase).invoke(
            argThat {
                answer == action.answer &&
                        question == action.question
            }
        )
    }

    @Test
    fun createFlashCard_shouldTriggerLoadingStateInitially() = runTest {
        invokeCollectingUiState {
            val action = FlashCardScreenAction.CreateFlashCard(
                question = faker.lorem().characters(),
                answer = faker.lorem().characters()
            )
            whenever(createFlashCardUseCase(any()))
                .then {
                    assertTrue(viewModel.uiState.value.progressState.creatingFlashCard)
                    UseCaseResponse.Success(Unit)
                }
            viewModel.processAction(action)

            assertFalse(viewModel.uiState.value.progressState.creatingFlashCard)
        }
    }

    @Test
    fun createFlashCard_whenUseCaseReturnsFailure_shouldTriggerErrorState() = runTest {
        invokeCollectingUiState {
            simulateCreateFolderErrorState()
            assertEquals(
                viewModel.uiState.value.flashCardScreenError,
                FlashCardScreenError.FailedToCreateFlashCard
            )
        }
    }

    @Test
    fun errorPresentedAction_shouldSetErrorStateToNull() = runTest {
        invokeCollectingUiState {
            simulateCreateFolderErrorState()
            viewModel.processAction(FlashCardScreenAction.FlashCardErrorPresented)
            assertNull(viewModel.uiState.value.flashCardScreenError)
        }
    }

    @Test
    fun removeFlashCard_shouldTriggerLoadingStateInitially() = runTest {
        invokeCollectingUiState {
            val action = FlashCardScreenAction.RemoveFlashCard(
                flashCard = FlashCard(
                    id = 10L,
                    question = faker.lorem().characters(),
                    answer = faker.lorem().characters(),
                    memorizationLevel = MemorizationLevel.HIGH
                )
            )
            whenever(removeFlashCardUseCase(any()))
                .then {
                    assertTrue(viewModel.uiState.value.progressState.removingFlashCard)
                    UseCaseResponse.Success(Unit)
                }
            viewModel.processAction(action)

            assertFalse(viewModel.uiState.value.progressState.removingFlashCard)
        }
    }

    @Test
    fun updateFlashCard_shouldTriggerLoadingStateInitially() = runTest {
        invokeCollectingUiState {
            val action = FlashCardScreenAction.UpdateFlashCard(
                flashCard = FlashCard(
                    id = 10L,
                    question = faker.lorem().characters(),
                    answer = faker.lorem().characters(),
                    memorizationLevel = MemorizationLevel.HIGH
                )
            )
            whenever(updateFlashCardUseCase(any()))
                .then {
                    assertTrue(viewModel.uiState.value.progressState.updatingFlashCard)
                    UseCaseResponse.Success(Unit)
                }
            viewModel.processAction(action)

            assertFalse(viewModel.uiState.value.progressState.updatingFlashCard)
        }
    }

    @Test
    fun removeFlashCard_whenUseCaseReturnsFailure_shouldTriggerErrorState() = runTest {
        invokeCollectingUiState {
            simulateRemoveFolderErrorState()
            assertEquals(
                viewModel.uiState.value.flashCardScreenError,
                FlashCardScreenError.FailedToRemoveFlashCard
            )
        }
    }

    @Test
    fun updateFlashCard_whenUseCaseReturnsFailure_shouldTriggerErrorState() = runTest {
        invokeCollectingUiState {
            simulateUpdateFolderErrorState()
            assertEquals(
                viewModel.uiState.value.flashCardScreenError,
                FlashCardScreenError.FailedToUpdateFlashCard
            )
        }
    }

    private suspend fun simulateCreateFolderErrorState() {
        val action = FlashCardScreenAction.CreateFlashCard(
            question = faker.lorem().characters(),
            answer = faker.lorem().characters()
        )
        whenever(createFlashCardUseCase(any()))
            .thenReturn(
                UseCaseResponse.Failure(
                    CreateFlashCardUseCaseFailure.CouldNotCreateFlashCard
                )
            )
        viewModel.processAction(action)
    }

    private suspend fun simulateRemoveFolderErrorState() {
        val action = FlashCardScreenAction.RemoveFlashCard(
            FlashCard(
                id = 10L,
                question = faker.lorem().characters(),
                answer = faker.lorem().characters(),
                memorizationLevel = MemorizationLevel.HIGH
            )
        )
        whenever(removeFlashCardUseCase(any()))
            .thenReturn(
                UseCaseResponse.Failure(UnitFailure)
            )
        viewModel.processAction(action)
    }

    private suspend fun simulateUpdateFolderErrorState() {
        val action = FlashCardScreenAction.UpdateFlashCard(
            FlashCard(
                id = 10L,
                question = faker.lorem().characters(),
                answer = faker.lorem().characters(),
                memorizationLevel = MemorizationLevel.HIGH
            )
        )
        whenever(updateFlashCardUseCase(any()))
            .thenReturn(
                UseCaseResponse.Failure(
                    UpdateFlashCardUseCaseFailure.UnknownErrorOccurred
                )
            )
        viewModel.processAction(action)
    }

    private fun invokeCollectingUiState(block: suspend () -> Unit) = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }
        block()
        collectJob.cancel()
    }
}