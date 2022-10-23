package uz.androdev.memorization.ui.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import uz.androdev.memorization.domain.response.UnitFailure
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
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

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        createFolderUseCase = mock()
        getFoldersUseCase = mock()

        whenever(getFoldersUseCase.invoke())
            .thenReturn(flowOf(emptyList()))

        viewModel = FoldersScreenViewModel(getFoldersUseCase, createFolderUseCase)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        whenever(getFoldersUseCase())
            .thenReturn(
                flow {
                    delay(1000)
                    emit(emptyList())
                }
            )
        viewModel = FoldersScreenViewModel(getFoldersUseCase, createFolderUseCase)

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        assertEquals(
            UiState(
                folders = null,
                failedToCreateFolder = false
            ), viewModel.uiState.value
        )
        job.cancel()
    }

    @Test
    fun createFolder_whenErrorOccurred_uiStateShouldNotifyError() = runTest {
        whenever(getFoldersUseCase.invoke())
            .thenReturn(flowOf(listOf(Folder(1, ""))))

        whenever(createFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UnitFailure))
        viewModel = FoldersScreenViewModel(getFoldersUseCase, createFolderUseCase)

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }
        viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))

        val uiState = viewModel.uiState.value
        assertEquals(true, uiState.failedToCreateFolder)

        job.cancel()
    }

    @Test
    fun whenErrorPresented_shouldChangeInternalState() = runTest {
        whenever(createFolderUseCase.invoke(any()))
            .thenReturn(UseCaseResponse.Failure(UnitFailure))
        viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        viewModel.processAction(Action.CreateFolder(FolderFactory.createUniqueFolderInput()))
        assertEquals(true, viewModel.uiState.value.failedToCreateFolder)

        viewModel.processAction(Action.FolderCreationFailurePresented)
        assertEquals(false, viewModel.uiState.value.failedToCreateFolder)

        job.cancel()
    }

    @Test
    fun uiState_getsFoldersFromUseCase() = runTest {
        val folders = MutableStateFlow<List<Folder>>(emptyList())
        whenever(getFoldersUseCase())
            .thenReturn(folders)
        viewModel = FoldersScreenViewModel(getFoldersUseCase, createFolderUseCase)

        val job = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect {}
        }

        assertEquals(folders.value, viewModel.uiState.value.folders)

        folders.emit(listOf(FolderFactory.createFolder()))
        assertEquals(folders.value, viewModel.uiState.value.folders)

        job.cancel()
    }

    @Test
    fun createFolder_delegatesToUseCase() = runTest {
        val folderInput = FolderFactory.createUniqueFolderInput()

        viewModel.processAction(Action.CreateFolder(folderInput))
        Mockito.verify(createFolderUseCase).invoke(eq(folderInput))
    }
}