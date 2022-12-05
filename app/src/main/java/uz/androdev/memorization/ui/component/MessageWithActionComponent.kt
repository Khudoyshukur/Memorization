package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.ui.theme.MemorizationTheme

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 3:43 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun MessageWithActionComponent(
    modifier: Modifier = Modifier,
    message: String,
    actionButtonText: String,
    onActionInvoked: () -> Unit
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            textAlign = TextAlign.Center,
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = onActionInvoked
        ) {
            Text(text = actionButtonText)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ErrorScreenPreview() {
    MemorizationTheme {
        MessageWithActionComponent(message = "Here is a message", actionButtonText = "Click me") {}
    }
}