package uz.androdev.memorization.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.R
import uz.androdev.memorization.model.model.Folder


/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 11:16 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    onFolderClicked: (folder: Folder) -> Unit = {},
    onNavigateToPracticeScreen: (folder: Folder) -> Unit = {},
    onUpdateFolded: (folder: Folder) -> Unit = {},
    onRemoveFolded: (folder: Folder) -> Unit = {},
) {
    var showDropDownMenu by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onFolderClicked(folder)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .height(24.dp)
                    .width(24.dp)
                    .align(Alignment.CenterVertically),
                painter = painterResource(R.drawable.ic_folder),
                contentDescription = stringResource(R.string.folder_icon)
            )
            Text(
                text = folder.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier.align(Alignment.CenterVertically)
            ){
                Icon(
                    modifier = Modifier
                        .height(24.dp)
                        .width(24.dp)
                        .clickable {
                            showDropDownMenu = true
                        },
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = stringResource(R.string.options_menu_icon)
                )

                DropdownMenu(
                    expanded = showDropDownMenu,
                    onDismissRequest = { showDropDownMenu = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.practice))
                        },
                        onClick = {
                            showDropDownMenu = false
                            onNavigateToPracticeScreen(folder)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.remove))
                        },
                        onClick = {
                            showDropDownMenu = false
                            onRemoveFolded(folder)
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.edit))
                        },
                        onClick = {
                            showDropDownMenu = false
                            onUpdateFolded(folder)
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun FolderItemPreview() {
    FolderItem(folder = Folder(1, "My awesome folder"))
}