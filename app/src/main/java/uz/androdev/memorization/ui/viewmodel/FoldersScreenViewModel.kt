package uz.androdev.memorization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.memorization.domain.response.onFailure
import uz.androdev.memorization.domain.response.onSuccess
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 2:52 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@HiltViewModel
class FoldersScreenViewModel @Inject constructor(
    private val getFoldersUseCase: GetFoldersUseCase,
    private val createFolderUseCase: CreateFolderUseCase
) : ViewModel() {

    private val failedToCreateFolderState = MutableStateFlow(false)

    val uiState: StateFlow<UiState> = combine(
        getFoldersUseCase(),
        failedToCreateFolderState
    ) { folders, failedToCreateFolder ->
        UiState(
            folders = folders,
            failedToCreateFolder = failedToCreateFolder
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = UiState()
    )

    fun processAction(action: Action) {
        viewModelScope.launch {
            when (action) {
                is Action.CreateFolder -> {
                    createFolder(action.folderInput)
                }
                Action.FolderCreationFailurePresented -> {
                    failedToCreateFolderState.emit(false)
                }
            }
        }
    }

    private suspend fun createFolder(folderInput: FolderInput) {
        val resp = createFolderUseCase(folderInput)
        resp.onFailure {
            failedToCreateFolderState.emit(true)
        }
    }

    companion object {
        private const val TAG = "FoldersScreenViewModel"
    }
}

data class UiState(
    val folders: List<Folder>? = null,
    val failedToCreateFolder: Boolean = false
)

sealed interface Action {
    data class CreateFolder(val folderInput: FolderInput) : Action
    object FolderCreationFailurePresented : Action
}