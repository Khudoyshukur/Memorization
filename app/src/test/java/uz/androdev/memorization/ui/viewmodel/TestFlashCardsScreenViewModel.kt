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
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCaseFailure
import uz.androdev.memorization.domain.usecase.GetFlashCardsUseCase
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.ui.navigation.Screen

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
        savedStateHandle = mock()

        whenever(getFlashCardsUseCase.invoke(any()))
            .thenReturn(flowOf(emptyList()))

        whenever(savedStateHandle.get<Long>(Screen.FlashCardsScreen.KEY_FOLDER_ID))
            .thenReturn(folderId)

        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase,
            createFlashCardUseCase,
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
            savedStateHandle
        )

        val state = viewModel.uiState.value

        assertEquals(
            state, FlashCardScreenUiState(
                flashCards = null,
                failedToCreateFlashCard = false,
                creatingFlashCard = false
            )
        )
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
        val action = FlashCardScreenAction.CreateFlashCard(
            question = faker.lorem().characters(),
            answer = faker.lorem().characters()
        )
        whenever(createFlashCardUseCase(any()))
            .then {
                assertTrue(viewModel.uiState.value.creatingFlashCard)
                UseCaseResponse.Success(Unit)
            }
        viewModel.processAction(action)

        assertFalse(viewModel.uiState.value.creatingFlashCard)
    }

    @Test
    fun createFlashCard_whenUseCaseReturnsFailure_shouldTriggerErrorState() = runTest {
        invokeCollectingUiState {
            simulateCreateFolderErrorState()
            assertTrue(viewModel.uiState.value.failedToCreateFlashCard)
        }
    }

    @Test
    fun errorPresentedAction_shouldSetErrorStateToFalse() = runTest {
        invokeCollectingUiState {
            simulateCreateFolderErrorState()
            viewModel.processAction(FlashCardScreenAction.CreateFlashCardFailurePresented)
            assertFalse(viewModel.uiState.value.failedToCreateFlashCard)
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

    private fun invokeCollectingUiState(block: suspend () -> Unit) = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }
        block()
        collectJob.cancel()
    }
}