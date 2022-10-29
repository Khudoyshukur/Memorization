package uz.androdev.memorization.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 28-10-2022
 * Time: 10:42 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardItem(
    modifier: Modifier = Modifier,
    flashCardOrderNumber: Int,
    flashCard: FlashCard,
    onFlashCardClicked: (flashCard: FlashCard) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onFlashCardClicked(flashCard)
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .width(50.dp)
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    text = flashCardOrderNumber.toString(),
                    fontWeight = FontWeight(700),
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            }
            Text(
                text = flashCard.question,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }
}

@Preview
@Composable
fun FlashCardItemPreview() {
    FlashCardItem(
        flashCardOrderNumber = 1000,
        flashCard = FlashCard(
            id = 100,
            question = "Some question",
            answer = "Answer to the question"
        )
    )
}