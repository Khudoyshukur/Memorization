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
import uz.androdev.memorization.model.input.FlashCardInput
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.navigation.Screen
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
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val folderId: Long =
        checkNotNull(savedStateHandle[Screen.FlashCardsScreen.KEY_FOLDER_ID])
    private val failedToCreateFlashCardState = MutableStateFlow(false)
    private val creatingFlashCardState = MutableStateFlow(false)

    val uiState: StateFlow<FlashCardScreenUiState> = combine(
        getFlashCardsUseCase(folderId),
        failedToCreateFlashCardState,
        creatingFlashCardState,
        ::FlashCardScreenUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = FlashCardScreenUiState()
    )

    fun processAction(action: FlashCardScreenAction) {
        viewModelScope.launch {
            when (action) {
                is FlashCardScreenAction.CreateFlashCard -> {
                    createFlashCard(action)
                }
                FlashCardScreenAction.CreateFlashCardFailurePresented -> {
                    failedToCreateFlashCardState.emit(false)
                }
            }
        }
    }

    private suspend fun createFlashCard(action: FlashCardScreenAction.CreateFlashCard) {
        if (creatingFlashCardState.value) return

        creatingFlashCardState.emit(true)
        val input = FlashCardInput(
            folderId = folderId,
            question = action.question,
            answer = action.answer
        )
        val resp = createFlashCardUseCase(input)
        resp.onFailure {
            failedToCreateFlashCardState.emit(true)
        }

        creatingFlashCardState.emit(false)
    }
}

sealed interface FlashCardScreenAction {
    data class CreateFlashCard(
        val question: String,
        val answer: String
    ) : FlashCardScreenAction

    object CreateFlashCardFailurePresented : FlashCardScreenAction
}

data class FlashCardScreenUiState(
    val flashCards: List<FlashCard>? = null,
    val failedToCreateFlashCard: Boolean = false,
    val creatingFlashCard: Boolean = false
)