package uz.androdev.memorization.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.memorization.domain.response.onFailure
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.GetFlashCardsUseCase
import uz.androdev.memorization.domain.usecase.RemoveFlashCardUseCase
import uz.androdev.memorization.domain.usecase.UpdateFlashCardUseCase
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.navigation.Arguments
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 26-10-2022
 * Time: 9:40 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class FlashCardsScreenViewModel @Inject constructor(
    private val getFlashCardsUseCase: GetFlashCardsUseCase,
    private val createFlashCardUseCase: CreateFlashCardUseCase,
    private val removeFlashCardUseCase: RemoveFlashCardUseCase,
    private val updateFlashCardUseCase: UpdateFlashCardUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val folderId: Long =
        checkNotNull(savedStateHandle[Arguments.ARGUMENT_FOLDER_ID])
    private val flashCardScreenErrorState = MutableStateFlow<FlashCardScreenError?>(null)
    private val creatingFlashCardState = MutableStateFlow(false)
    private val updatingFlashCardState = MutableStateFlow(false)
    private val removingFlashCardState = MutableStateFlow(false)
    private val progressState = combine(
        creatingFlashCardState,
        updatingFlashCardState,
        removingFlashCardState,
        ::ProgressState
    )

    val uiState: StateFlow<FlashCardScreenUiState> = combine(
        getFlashCardsUseCase(folderId),
        flashCardScreenErrorState,
        progressState,
        ::FlashCardScreenUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = FlashCardScreenUiState()
    )

    fun processAction(action: FlashCardScreenAction) {
        when (action) {
            is FlashCardScreenAction.CreateFlashCard -> {
                createFlashCard(action)
            }
            FlashCardScreenAction.FlashCardErrorPresented -> {
                flashCardScreenErrorState.update { null }
            }
            is FlashCardScreenAction.UpdateFlashCard -> updateFlashCard(action.flashCard)
            is FlashCardScreenAction.RemoveFlashCard -> removeFlashCard(action.flashCard)
        }
    }

    private fun createFlashCard(action: FlashCardScreenAction.CreateFlashCard) {
        if (creatingFlashCardState.value) return

        creatingFlashCardState.update { true }
        val input = FlashCardInput(
            folderId = folderId,
            question = action.question,
            answer = action.answer
        )
        viewModelScope.launch {
            val resp = createFlashCardUseCase(input)
            resp.onFailure {
                flashCardScreenErrorState.emit(FlashCardScreenError.FailedToCreateFlashCard)
            }
            creatingFlashCardState.emit(false)
        }
    }

    private fun updateFlashCard(flashCard: FlashCard) {
        if (updatingFlashCardState.value) return

        updatingFlashCardState.update { true }
        viewModelScope.launch {
            updateFlashCardUseCase(flashCard).onFailure {
                flashCardScreenErrorState.emit(FlashCardScreenError.FailedToUpdateFlashCard)
            }
            updatingFlashCardState.emit(false)
        }
    }

    private fun removeFlashCard(flashCard: FlashCard) {
        if (removingFlashCardState.value) return

        removingFlashCardState.update { true }
        viewModelScope.launch {
            removeFlashCardUseCase(flashCard).onFailure {
                flashCardScreenErrorState.emit(FlashCardScreenError.FailedToRemoveFlashCard)
            }
            removingFlashCardState.emit(false)
        }
    }
}

sealed interface FlashCardScreenAction {
    data class CreateFlashCard(
        val question: String,
        val answer: String
    ) : FlashCardScreenAction

    data class UpdateFlashCard(val flashCard: FlashCard) : FlashCardScreenAction
    data class RemoveFlashCard(val flashCard: FlashCard) : FlashCardScreenAction

    object FlashCardErrorPresented : FlashCardScreenAction
}

data class FlashCardScreenUiState(
    val flashCards: List<FlashCard>? = null,
    val flashCardScreenError: FlashCardScreenError? = null,
    val progressState: ProgressState = ProgressState()
)

data class ProgressState(
    val creatingFlashCard: Boolean = false,
    val updatingFlashCard: Boolean = false,
    val removingFlashCard: Boolean = false
)

sealed interface FlashCardScreenError {
    object FailedToCreateFlashCard : FlashCardScreenError
    object FailedToUpdateFlashCard : FlashCardScreenError
    object FailedToRemoveFlashCard : FlashCardScreenError
}