package uz.androdev.memorization.ui.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
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
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.*
import uz.androdev.memorization.factory.FolderFactory
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 5:48 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestFoldersScreenViewModel {
    private lateinit var viewModel: FoldersScreenViewModel
    private lateinit var createFolderUseCase: CreateFolderUseCase
    private lateinit var getFoldersUseCase: GetFoldersUseCase
    private lateinit var updateFolderUseCase: UpdateFolderUseCase
    private lateinit var removeFolderUseCase: RemoveFolderUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        createFolderUseCase = mock()
        getFoldersUseCase = mock()
        updateFolderUseCase = mock()
        removeFolderUseCase = mock()

        whenever(getFoldersUseCase.invoke())
            .thenReturn(flowOf(emptyList()))

        viewModel = FoldersScreenViewModel(
            getFoldersUseCase,
            createFolderUseCase,
            updateFolderUseCase,
            removeFolderUseCase
        )
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        whenever(getFoldersUseCase())
            .then {
                assertEquals(viewModel.uiState.value.folders, null)
                assertEquals(viewModel.uiState.value.foldersScreenError, null)
                assertEquals(viewModel.uiState.value.progress, FoldersScreenProgressState())
                flowOf(emptyList<Folder>())
            }
        viewModel = FoldersScreenViewModel(
            getFoldersUseCase,
            createFolderUseCase,
            updateFolderUseCase,
            removeFolderUseCase
        )

        invokeCollectingUiState {
            val uiState = viewModel.uiState.first()
            assertEquals(uiState.folders, emptyList<Folder>())
            assertEquals(uiState.foldersScreenError, null)
            assertEquals(uiState.progress, FoldersScreenProgressState())
        }
    }

    @Test
    fun uiState_getsFoldersFromUseCase() = runTest {
        val folders = MutableStateFlow<List<Folder>>(emptyList())
        whenever(getFoldersUseCase())
            .thenReturn(folders)
        viewModel = FoldersScreenViewModel(
            getFoldersUseCase,
            createFolderUseCase,
            updateFolderUseCase,
            removeFolderUseCase
        )

        invokeCollectingUiState {
            assertEquals(folders.value, viewModel.uiState.value.folders)

            folders.emit(listOf(FolderFactory.createFolder()))
            assertEquals(folders.value, viewModel.uiState.value.folders)
        }
    }

    @Test
    fun createFolder_delegatesToUseCase() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()

        viewModel.processAction(Action.CreateFolder(folderInput))
        Mockito.verify(createFolderUseCase).invoke(eq(folderInput))
    }

    @Test
    fun updateFolder_delegatesToUseCase() = runTest {
        val folder = FolderFactory.createFolder()

        viewModel.processAction(Action.UpdateFolder(folder))
        Mockito.verify(updateFolderUseCase).invoke(eq(folder))
    }

    @Test
    fun removeFolder_delegatesToUseCase() = runTest {
        val folder = FolderFactory.createFolder()

        viewModel.processAction(Action.RemoveFolder(folder))
        Mockito.verify(removeFolderUseCase).invoke(eq(folder))
    }

    @Test
    fun createFolder_shouldUpdateProgressState() = runTest {
        whenever(createFolderUseCase.invoke(any()))
            .then {
                assertTrue(viewModel.uiState.value.progress.creatingFolder)
                UseCaseResponse.Success(Unit)
            }

        invokeCollectingUiState {
            viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))
            assertFalse(viewModel.uiState.value.progress.creatingFolder)
        }
    }

    @Test
    fun updateFolder_shouldUpdateProgressState() = runTest {
        whenever(updateFolderUseCase.invoke(any()))
            .then {
                assertTrue(viewModel.uiState.value.progress.updatingFolder)
                UseCaseResponse.Success(Unit)
            }

        invokeCollectingUiState {
            viewModel.processAction(Action.UpdateFolder(FolderFactory.createFolder()))
            assertFalse(viewModel.uiState.value.progress.updatingFolder)
        }
    }

    @Test
    fun removeFolder_shouldUpdateProgressState() = runTest {
        whenever(removeFolderUseCase.invoke(any()))
            .then {
                assertTrue(viewModel.uiState.value.progress.removingFolder)
                UseCaseResponse.Success(Unit)
            }

        invokeCollectingUiState {
            viewModel.processAction(Action.RemoveFolder(FolderFactory.createFolder()))
            assertFalse(viewModel.uiState.value.progress.removingFolder)
        }
    }

    @Test
    fun createFolder_whenErrorOccurred_uiStateShouldNotifyError() = runTest {
        whenever(createFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UnitFailure))

        invokeCollectingUiState {
            viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))

            val uiState = viewModel.uiState.value
            assertEquals(uiState.foldersScreenError, FoldersScreenError.FailedToCreateFolder)
        }
    }

    @Test
    fun updateFolder_whenErrorOccurred_uiStateShouldNotifyError() = runTest {
        whenever(updateFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UpdateFolderUseCaseFailure.UnknownError))


        invokeCollectingUiState {
            viewModel.processAction(Action.UpdateFolder(FolderFactory.createFolder()))

            val uiState = viewModel.uiState.value
            assertEquals(uiState.foldersScreenError, FoldersScreenError.FailedToUpdateFolder)
        }
    }

    @Test
    fun removeFolder_whenErrorOccurred_uiStateShouldNotifyError() = runTest {
        whenever(removeFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(RemoveFolderUseCaseFailure.UnknownError))


        invokeCollectingUiState {
            viewModel.processAction(Action.RemoveFolder(FolderFactory.createFolder()))

            val uiState = viewModel.uiState.value
            assertEquals(uiState.foldersScreenError, FoldersScreenError.FailedToRemoveFolder)
        }
    }

    @Test
    fun whenErrorPresented_shouldChangeInternalState() = runTest {
        whenever(createFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UnitFailure))
        viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))

        invokeCollectingUiState {
            viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))
            assertEquals(
                viewModel.uiState.value.foldersScreenError,
                FoldersScreenError.FailedToCreateFolder
            )

            viewModel.processAction(Action.FoldersScreenErrorPresented)
            assertEquals(viewModel.uiState.value.foldersScreenError, null)
        }
    }

    private fun invokeCollectingUiState(block: suspend () -> Unit) = runTest {
        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }
        block()
        job.cancel()
    }
}