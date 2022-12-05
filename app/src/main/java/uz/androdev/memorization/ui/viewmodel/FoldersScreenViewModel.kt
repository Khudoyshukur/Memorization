package uz.androdev.memorization.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uz.androdev.memorization.data.repository.FolderRepository
import uz.androdev.memorization.domain.response.onFailure
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
import uz.androdev.memorization.domain.usecase.RemoveFolderUseCase
import uz.androdev.memorization.domain.usecase.UpdateFolderUseCase
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
    private val createFolderUseCase: CreateFolderUseCase,
    private val updateFolderUseCase: UpdateFolderUseCase,
    private val removeFolderUseCase: RemoveFolderUseCase
) : ViewModel() {

    private val creatingFolderState = MutableStateFlow(false)
    private val updatingFolderState = MutableStateFlow(false)
    private val removingFolderState = MutableStateFlow(false)
    private val progressState = combine(
        creatingFolderState,
        updatingFolderState,
        removingFolderState,
        ::FoldersScreenProgressState
    )

    private val foldersScreenError = MutableStateFlow<FoldersScreenError?>(null)

    val uiState: StateFlow<UiState> = combine(
        getFoldersUseCase(),
        foldersScreenError,
        progressState,
        ::UiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = UiState()
    )

    fun processAction(action: Action) {
        when (action) {
            is Action.CreateFolder -> createFolder(action.folderInput)
            is Action.RemoveFolder -> removeFolder(action.folder)
            is Action.UpdateFolder -> updateFolder(action.folder)
            Action.FoldersScreenErrorPresented -> {
                foldersScreenError.update { null }
            }
        }
    }

    private fun createFolder(folderInput: FolderInput) {
        if (creatingFolderState.value) return

        creatingFolderState.update { true }
        viewModelScope.launch {
            val resp = createFolderUseCase(folderInput)
            resp.onFailure {
                foldersScreenError.emit(FoldersScreenError.FailedToCreateFolder)
            }
            creatingFolderState.emit(false)
        }
    }

    private fun updateFolder(folder: Folder) {
        if (updatingFolderState.value) return

        updatingFolderState.update { true }
        viewModelScope.launch {
            val resp = updateFolderUseCase(folder)
            resp.onFailure {
                foldersScreenError.emit(FoldersScreenError.FailedToUpdateFolder)
            }
            updatingFolderState.emit(false)
        }
    }

    private fun removeFolder(folder: Folder) {
        if (removingFolderState.value) return

        removingFolderState.update { true }
        viewModelScope.launch {
            val resp = removeFolderUseCase(folder)
            resp.onFailure {
                foldersScreenError.emit(FoldersScreenError.FailedToRemoveFolder)
            }
            removingFolderState.emit(false)
        }
    }

    companion object {
        private const val TAG = "FoldersScreenViewModel"
    }
}

data class UiState(
    val folders: List<Folder>? = null,
    val foldersScreenError: FoldersScreenError? = null,
    val progress: FoldersScreenProgressState = FoldersScreenProgressState()
)

data class FoldersScreenProgressState(
    val creatingFolder: Boolean = false,
    val updatingFolder: Boolean = false,
    val removingFolder: Boolean = false,
)

sealed interface FoldersScreenError {
    object FailedToCreateFolder : FoldersScreenError
    object FailedToUpdateFolder : FoldersScreenError
    object FailedToRemoveFolder : FoldersScreenError
}

sealed interface Action {
    data class CreateFolder(val folderInput: FolderInput) : Action
    data class UpdateFolder(val folder: Folder) : Action
    data class RemoveFolder(val folder: Folder) : Action
    object FoldersScreenErrorPresented : Action
}