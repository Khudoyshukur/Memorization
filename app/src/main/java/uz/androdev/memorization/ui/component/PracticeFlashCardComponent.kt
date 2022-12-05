package uz.androdev.memorization.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.R
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.theme.MemorizationTheme

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 11:15 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun PracticeFlashCardComponent(
    modifier: Modifier = Modifier,
    flashCard: FlashCard
) {
    var showAnswer by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = flashCard.question,
            style = MaterialTheme.typography.titleMedium
        )
        AnimatedVisibility(visible = showAnswer) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = flashCard.answer,
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showAnswer = !showAnswer
            }
        ) {
            Text(
                text = if (showAnswer) {
                    stringResource(id = R.string.hide_answer)
                } else {
                    stringResource(id = R.string.show_answer)
                }
            )
        }
    }
}

@Preview(showSystemUi = false)
@Composable
fun PracticeFlashCardComponentPreview() {
    MemorizationTheme {
        PracticeFlashCardComponent(
            flashCard = FlashCard(
                id = 10,
                question = "Who are you!",
                answer = "I am developer",
                memorizationLevel = MemorizationLevel.HIGH
            )
        )
    }
}