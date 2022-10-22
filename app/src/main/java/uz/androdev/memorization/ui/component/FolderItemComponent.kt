package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.model.model.Folder

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 11:16 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    onFolderClicked: (folder: Folder) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onFolderClicked(folder)
        }
    ) {
        Text(
            text = folder.title,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun FolderItemPreview() {
    FolderItem(folder = Folder(1, "My awesome folder"))
}