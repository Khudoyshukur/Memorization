package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    onFolderClicked: (folder: Folder) -> Unit = {}
) {
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
                modifier = Modifier.padding(horizontal = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun FolderItemPreview() {
    FolderItem(folder = Folder(1, "My awesome folder"))
}