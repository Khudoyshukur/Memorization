package uz.androdev.memorization.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uz.androdev.memorization.domain.util.ModelInput
import uz.androdev.memorization.domain.util.PracticeModelState
import uz.androdev.memorization.domain.util.PracticeModel
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.ui.navigation.Arguments
import uz.androdev.memorization.ui.navigation.Arguments.ARGUMENT_FOLDER_ID
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 12:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class PracticeFlashCardsScreenViewModel @Inject constructor(
    private val practiceModel: PracticeModel,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val folderId: Long =
        checkNotNull(savedStateHandle[ARGUMENT_FOLDER_ID])
    val practiceModelState get() = practiceModel.state

    init {
        processAction(PracticeFlashCardsScreenAction.StartPractice)
    }

    fun processAction(action: PracticeFlashCardsScreenAction) = viewModelScope.launch {
        when (action) {
            PracticeFlashCardsScreenAction.StartPractice -> {
                practiceModel.processInput(ModelInput.StartPractice(folderId))
            }
            is PracticeFlashCardsScreenAction.Next -> {
                practiceModel.processInput(ModelInput.Next(action.memorizationLevel))
            }
        }
    }
}

sealed interface PracticeFlashCardsScreenAction {
    object StartPractice : PracticeFlashCardsScreenAction
    data class Next(val memorizationLevel: MemorizationLevel) : PracticeFlashCardsScreenAction
}