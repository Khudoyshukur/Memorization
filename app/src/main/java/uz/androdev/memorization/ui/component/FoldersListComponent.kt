package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.R
import uz.androdev.memorization.model.model.Folder
import java.util.*

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 2:45 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun FoldersListComponent(
    folders: List<Folder>?,
    onFolderClicked: (Folder) -> Unit = {}
) {
    if (folders == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.Center)
                    .testTag(stringResource(id = R.string.progress_bar)),
            )
        }
    } else {
        if (folders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                NoItemsComponent(
                    modifier = Modifier.align(Alignment.Center),
                    message = stringResource(id = R.string.no_folders_message)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .testTag(stringResource(id = R.string.folders_list))
            ) {
                items(items = folders, key = { it.id }) {
                    FolderItem(
                        folder = it,
                        onFolderClicked = onFolderClicked
                    )
                    Divider(thickness = 16.dp, color = Color.Transparent)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ComponentPreview() {
    FoldersListComponent(
        folders = List(0) {
            Folder(
                (Long.MIN_VALUE..Long.MAX_VALUE).random(),
                UUID.randomUUID().toString().substring(0, 10)
            )
        }
    )
}