package uz.androdev.memorization.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import uz.androdev.memorization.R
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.component.CreateFlashCardDialog
import uz.androdev.memorization.ui.component.FlashCardsListComponent
import uz.androdev.memorization.ui.theme.MemorizationTheme
import uz.androdev.memorization.ui.viewmodel.FlashCardScreenAction
import uz.androdev.memorization.ui.viewmodel.FlashCardsScreenViewModel
import java.util.UUID

/**
 * Created by: androdev
 * Date: 28-10-2022
 * Time: 10:41 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun FlashCardScreenRoute(
    viewModel: FlashCardsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    FlashCardsScreen(
        flashCards = uiState.flashCards,
        onFlashCardClicked = {},
        onCreateFlashCard = { question, answer ->
            val action = FlashCardScreenAction.CreateFlashCard(
                question = question,
                answer = answer
            )
            viewModel.processAction(action)
        }
    )

    LaunchedEffect(key1 = uiState.failedToCreateFlashCard) {
        if (uiState.failedToCreateFlashCard) {
            Toast.makeText(
                context,
                context.getString(R.string.operation_failed),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardsScreen(
    flashCards: List<FlashCard>?,
    onFlashCardClicked: (FlashCard) -> Unit,
    onCreateFlashCard: (question: String, answer: String) -> Unit
) {
    var showCreateFlashCardDialog by remember {
        mutableStateOf(false)
    }

    Scaffold(
        content = {
            FlashCardsListComponent(
                modifier = Modifier
                    .padding(it),
                flashCards = flashCards,
                onFlashCardClicked = onFlashCardClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateFlashCardDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add_flash_card)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    )

    if (showCreateFlashCardDialog) {
        CreateFlashCardDialog(
            onDismissRequested = { showCreateFlashCardDialog = false },
            onCreateFlashCard = { question, answer ->
                showCreateFlashCardDialog = false
                onCreateFlashCard(question, answer)
            }
        )
    }
}

@Preview
@Composable
fun FlashCardScreenPreview() {
    MemorizationTheme {
        FlashCardsScreen(
            flashCards = List(10) {
                FlashCard(
                    id = it.toLong(),
                    question = UUID.randomUUID().toString(),
                    answer = UUID.randomUUID().toString()
                )
            }, onFlashCardClicked = {}, onCreateFlashCard = { _, _ -> }
        )
    }
}