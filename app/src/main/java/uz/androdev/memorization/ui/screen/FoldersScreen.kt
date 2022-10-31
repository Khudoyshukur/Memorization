package uz.androdev.memorization.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import uz.androdev.memorization.R
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder
import uz.androdev.memorization.ui.component.FolderDialog
import uz.androdev.memorization.ui.component.FoldersListComponent
import uz.androdev.memorization.ui.viewmodel.Action
import uz.androdev.memorization.ui.viewmodel.FoldersScreenError
import uz.androdev.memorization.ui.viewmodel.FoldersScreenViewModel

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 2:51 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun FolderScreenRoute(
    onNavigateToItemsScreen: (Folder) -> Unit,
    viewModel: FoldersScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    FoldersScreen(
        folders = uiState.folders,
        onFolderClicked = onNavigateToItemsScreen,
        onCreateFolder = {
            viewModel.processAction(Action.CreateFolder(it))
        },
        onUpdateFolder = {
            viewModel.processAction(Action.UpdateFolder(it))
        },
        onRemoveFolder = {
            viewModel.processAction(Action.RemoveFolder(it))
        },
    )

    LaunchedEffect(key1 = uiState.foldersScreenError) {
        when (uiState.foldersScreenError ?: return@LaunchedEffect) {
            FoldersScreenError.FailedToCreateFolder,
            FoldersScreenError.FailedToRemoveFolder,
            FoldersScreenError.FailedToUpdateFolder -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.operation_failed),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.processAction(Action.FoldersScreenErrorPresented)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    folders: List<Folder>?,
    onFolderClicked: (Folder) -> Unit = {},
    onCreateFolder: (FolderInput) -> Unit = {},
    onRemoveFolder: (Folder) -> Unit = {},
    onUpdateFolder: (Folder) -> Unit = {}
) {
    var showCreateFolderDialog by remember {
        mutableStateOf(false)
    }

    var folderToEdit by remember {
        mutableStateOf<Folder?>(null)
    }

    Scaffold(
        content = {
            FoldersListComponent(
                modifier = Modifier.padding(it),
                folders = folders,
                onFolderClicked = onFolderClicked,
                onRemoveFolded = onRemoveFolder,
                onUpdateFolded = { folder ->
                    folderToEdit = folder
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateFolderDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_folder)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    )

    if (showCreateFolderDialog) {
        FolderDialog(
            titleText = stringResource(R.string.create_folder),
            positiveButtonText = stringResource(R.string.create),
            negativeButtonText = stringResource(R.string.cancel),
            onDismissRequested = { showCreateFolderDialog = false },
            onPositiveButtonClicked = {
                showCreateFolderDialog = false
                onCreateFolder(it)
            },
        )
    }

    folderToEdit?.let { folder ->
        FolderDialog(
            titleText = stringResource(R.string.edit_folder),
            positiveButtonText = stringResource(R.string.edit),
            negativeButtonText = stringResource(R.string.cancel),
            initialFolderName = folder.title,
            onDismissRequested = { folderToEdit = null },
            onPositiveButtonClicked = { input ->
                folderToEdit = null
                onUpdateFolder(folder.copy(title = input.title))
            },
        )
    }
}

@Preview
@Composable
fun ScreenPreview() {
    FoldersScreen(folders = emptyList(), onFolderClicked = {})
}