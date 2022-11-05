package uz.androdev.memorization.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uz.androdev.memorization.R
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import java.util.UUID

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 12:05 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun FlashCardsListComponent(
    modifier: Modifier = Modifier,
    flashCards: List<FlashCard>?,
    onFlashCardClicked: (FlashCard) -> Unit = {}
) {
    if (flashCards == null) {
        Box(modifier = modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(50.dp)
                    .width(50.dp)
                    .align(Alignment.Center)
                    .testTag(stringResource(id = R.string.progress_bar)),
            )
        }
    } else {
        if (flashCards.isEmpty()) {
            Box(modifier = modifier.fillMaxSize()) {
                NoItemsComponent(
                    modifier = Modifier.align(Alignment.Center),
                    message = stringResource(id = R.string.no_flash_cards_message)
                )
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(16.dp)
                    .testTag(stringResource(id = R.string.flash_cards_list))
            ) {
                itemsIndexed(
                    items = flashCards,
                    key = { _, item -> item.id }
                ) { index, item ->
                    FlashCardItem(
                        flashCardOrderNumber = index + 1,
                        flashCard = item,
                        onFlashCardClicked = onFlashCardClicked
                    )
                    Divider(thickness = 16.dp, color = Color.Transparent)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun FlashCardsListComponentPreview() {
    FlashCardsListComponent(
        flashCards = List(20) {
            FlashCard(
                id = it.toLong(),
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString(),
                memorizationLevel = MemorizationLevel.HIGH
            )
        }
    )
}