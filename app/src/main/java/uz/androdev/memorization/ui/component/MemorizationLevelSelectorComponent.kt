package uz.androdev.memorization.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ContentAlpha.medium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uz.androdev.memorization.R
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.ui.theme.MemorizationTheme

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 3:02 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun MemorizationLevelSelectorComponent(
    modifier: Modifier = Modifier,
    onMemorizationLevelSelected: (MemorizationLevel) -> Unit
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red
            ),
            onClick = {
                onMemorizationLevelSelected(MemorizationLevel.LOW)
            }
        ) {
            Text(
                text = stringResource(id = R.string.low),
                color = Color.Black
            )
        }
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Yellow
            ),
            onClick = {
                onMemorizationLevelSelected(MemorizationLevel.MEDIUM)
            }
        ) {
            Text(
                text = stringResource(id = R.string.medium),
                color = Color.Black
            )
        }
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green
            ),
            onClick = {
                onMemorizationLevelSelected(MemorizationLevel.HIGH)
            }
        ) {
            Text(
                text = stringResource(id = R.string.high),
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun MemorizationLevelSelectorComponentPreview() {
    MemorizationTheme {
        MemorizationLevelSelectorComponent(onMemorizationLevelSelected = {})
    }
}