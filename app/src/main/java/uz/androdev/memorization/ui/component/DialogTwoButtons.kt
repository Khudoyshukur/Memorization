package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import uz.androdev.memorization.R
import uz.androdev.memorization.model.input.FolderInput

/**
 * Created by: androdev
 * Date: 23-10-2022
 * Time: 11:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun DialogTwoButtons(
    modifier: Modifier = Modifier,
    negativeButtonText: String,
    positiveButtonText: String,
    onNegativeButtonClicked: () -> Unit,
    onPositiveButtonClicked: () -> Unit,
) {
    Row(modifier = modifier) {
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onNegativeButtonClicked,
        ) { Text(text = negativeButtonText) }
        TextButton(
            modifier = Modifier.weight(1f),
            onClick = onPositiveButtonClicked
        ) { Text(text = positiveButtonText) }
    }
}