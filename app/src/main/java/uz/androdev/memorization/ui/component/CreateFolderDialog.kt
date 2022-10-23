package uz.androdev.memorization.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import uz.androdev.memorization.R
import uz.androdev.memorization.model.input.FolderInput

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 3:21 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateFolderDialog(
    onDismissRequested: () -> Unit,
    onCreateFolder: (FolderInput) -> Unit
) {
    var input by remember {
        mutableStateOf("")
    }
    var showInputIsBlankMessageError by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismissRequested,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .testTag(stringResource(R.string.create_folder_dialog))
                .wrapContentHeight()
                .animateContentSize()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.create_folder),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .testTag(stringResource(R.string.create_folder_input_field))
                        .fillMaxWidth(),
                    value = input,
                    onValueChange = {
                        showInputIsBlankMessageError = false
                        input = it
                    },
                    trailingIcon = {
                        if (showInputIsBlankMessageError) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = stringResource(R.string.error_icon),
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    isError = showInputIsBlankMessageError,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
                )
                if (showInputIsBlankMessageError) {
                    Text(
                        text = stringResource(R.string.please_fill_the_field),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                DialogTwoButtons(
                    negativeButtonText = stringResource(id = R.string.cancel),
                    positiveButtonText = stringResource(id = R.string.create),
                    onNegativeButtonClicked = { onDismissRequested() },
                    onPositiveButtonClicked = {
                        if (input.isBlank()) {
                            showInputIsBlankMessageError = true
                        } else {
                            val folderInput = FolderInput(
                                title = input.trim()
                            )
                            onCreateFolder(folderInput)
                        }
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun AddFolderDialogPreview() {
    CreateFolderDialog(onDismissRequested = {}, onCreateFolder = {})
}