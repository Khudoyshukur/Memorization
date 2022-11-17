package uz.androdev.memorization.domain.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.GetPracticeFlashCardsUseCase
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 08-11-2022
 * Time: 9:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class PracticeModel @Inject constructor(
    private val getPracticeFlashCardsUseCase: GetPracticeFlashCardsUseCase
) {
    private val _state = MutableStateFlow<PracticeModelState>(PracticeModelState.Idle)
    val state: StateFlow<PracticeModelState> get() = _state.asStateFlow()

    private var flashCards: List<FlashCard> = emptyList()
    private var currentFlashCardIndex = -1

    private var inputIsBeingProcessed: Boolean = false

    suspend fun processInput(input: ModelInput) {
        if (inputIsBeingProcessed) return

        inputIsBeingProcessed = true
        when (input) {
            is ModelInput.StartPractice -> start(input.folderId)
            is ModelInput.Next -> next()
        }
        inputIsBeingProcessed = false
    }

    private suspend fun start(folderId: Long) {
        _state.emit(PracticeModelState.Initializing)

        val resp = getPracticeFlashCardsUseCase.invoke(folderId)
        if (resp is UseCaseResponse.Success) {
            if (resp.data.isEmpty()) {
                _state.emit(PracticeModelState.EmptyFlashCards)
            } else {
                flashCards = resp.data
                _state.emit(PracticeModelState.Practice(flashCards[++currentFlashCardIndex]))
            }
        } else {
            _state.emit(PracticeModelState.FailedToInitialize)
        }
    }

    private suspend fun next() {
        if (state.value !is PracticeModelState.Practice) {
            throw IllegalStateException("Current state ${state.value}")
        }

        if (hasNext()) {
            _state.emit(PracticeModelState.Practice(flashCards[++currentFlashCardIndex]))
        } else {
            _state.emit(PracticeModelState.Finished)
        }
    }

    private fun hasNext(): Boolean {
        return currentFlashCardIndex + 1 < flashCards.size
    }
}

sealed interface ModelInput {
    data class StartPractice(val folderId: Long) : ModelInput
    data class Next(val answer: MemorizationLevel) : ModelInput
}

sealed interface PracticeModelState {
    object Idle : PracticeModelState
    object Initializing : PracticeModelState
    object FailedToInitialize : PracticeModelState
    object EmptyFlashCards : PracticeModelState
    data class Practice(val flashCard: FlashCard) : PracticeModelState
    object Finished : PracticeModelState
}