package uz.androdev.memorization.ui.screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import uz.androdev.memorization.R
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.component.FlashCardDialog
import uz.androdev.memorization.ui.component.FlashCardsListComponent
import uz.androdev.memorization.ui.theme.MemorizationTheme
import uz.androdev.memorization.ui.viewmodel.FlashCardScreenAction
import uz.androdev.memorization.ui.viewmodel.FlashCardScreenError
import uz.androdev.memorization.ui.viewmodel.FlashCardsScreenViewModel
import java.util.*

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
        onCreateFlashCard = { question, answer ->
            val action = FlashCardScreenAction.CreateFlashCard(
                question = question,
                answer = answer
            )
            viewModel.processAction(action)
        },
        onRemoveFlashCard = {
            val action = FlashCardScreenAction.RemoveFlashCard(it)
            viewModel.processAction(action)
        },
        onUpdateFlashCard = {
            val action = FlashCardScreenAction.UpdateFlashCard(it)
            viewModel.processAction(action)
        }
    )

    LaunchedEffect(key1 = uiState.flashCardScreenError) {
        when (uiState.flashCardScreenError ?: return@LaunchedEffect) {
            FlashCardScreenError.FailedToCreateFlashCard,
            FlashCardScreenError.FailedToRemoveFlashCard,
            FlashCardScreenError.FailedToUpdateFlashCard -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.operation_failed),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.processAction(FlashCardScreenAction.FlashCardErrorPresented)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FlashCardsScreen(
    flashCards: List<FlashCard>?,
    onCreateFlashCard: (question: String, answer: String) -> Unit,
    onUpdateFlashCard: (FlashCard) -> Unit = {},
    onRemoveFlashCard: (FlashCard) -> Unit = {}
) {

    val selectedFlashCard = remember { mutableStateOf<FlashCard?>(null) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    var flashCardToEdit by remember {
        mutableStateOf<FlashCard?>(null)
    }

    if (sheetState.isVisible) {
        BackHandler {
            coroutineScope.launch { sheetState.hide() }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            selectedFlashCard.value.let {
                if (it == null) {
                    EmptySheet()
                } else {
                    FlashCardDetailsBottomSheet(
                        modifier = Modifier
                            .testTag(stringResource(R.string.flash_card_details_sheet)),
                        flashCard = it,
                        onDismissRequested = {
                            coroutineScope.launch {
                                sheetState.hide()
                            }
                        },
                        onUpdateFlashCard = {
                            coroutineScope.launch {
                                sheetState.hide()
                                selectedFlashCard.value = null
                                flashCardToEdit = it
                            }
                        },
                        onRemoveFlashCard = {
                            coroutineScope.launch {
                                sheetState.hide()
                                selectedFlashCard.value = null
                                onRemoveFlashCard(it)
                            }
                        }
                    )
                }
            }
        },
        sheetState = sheetState
    ) {
        FlashCardsMainContent(
            flashCards = flashCards,
            onCreateFlashCard = onCreateFlashCard,
            onFlashCardClicked = { clickedFlashCard ->
                selectedFlashCard.value = clickedFlashCard
                coroutineScope.launch { sheetState.show() }
            },
        )
    }

    if (flashCardToEdit != null) {
        FlashCardDialog(
            title = stringResource(id = R.string.edit_flash_card),
            negativeButtonText = stringResource(id = R.string.cancel),
            positiveButtonText = stringResource(id = R.string.edit),
            initialQuestionText = flashCardToEdit?.question ?: "",
            initialAnswerText = flashCardToEdit?.answer ?: "",
            onDismissRequested = {
                flashCardToEdit = null
            },
            onSubmit = { question, answer ->
                flashCardToEdit?.let {
                    flashCardToEdit = null

                    onUpdateFlashCard(
                        it.copy(
                            question = question,
                            answer = answer
                        )
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashCardsMainContent(
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
        FlashCardDialog(
            title = stringResource(id = R.string.create_flash_card),
            negativeButtonText = stringResource(id = R.string.cancel),
            positiveButtonText = stringResource(id = R.string.create),
            onDismissRequested = { showCreateFlashCardDialog = false },
            onSubmit = { question, answer ->
                showCreateFlashCardDialog = false
                onCreateFlashCard(question, answer)
            }
        )
    }
}

@Composable
fun EmptySheet() {
    Box(modifier = Modifier.height(1.dp))
}

@Composable
fun FlashCardDetailsBottomSheet(
    modifier: Modifier = Modifier,
    flashCard: FlashCard,
    onDismissRequested: () -> Unit = {},
    onUpdateFlashCard: (FlashCard) -> Unit = {},
    onRemoveFlashCard: (FlashCard) -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.question),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = flashCard.question,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(R.string.answer),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = flashCard.answer)
                }

                Icon(
                    modifier = Modifier
                        .clickable(enabled = true) {
                            onDismissRequested()
                        },
                    imageVector = Icons.Filled.Clear,
                    contentDescription = stringResource(R.string.close_sheet),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = { onRemoveFlashCard(flashCard) }
                ) {
                    Text(text = stringResource(R.string.remove))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 6.dp),
                    onClick = { onUpdateFlashCard(flashCard) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary
                    )
                ) {
                    Text(text = stringResource(R.string.edit))
                }
            }
        }
    }
}

@Preview
@Composable
fun FlashCardDetailsComponentPreview() {
    MemorizationTheme {
        FlashCardDetailsBottomSheet(
            flashCard = FlashCard(
                id = 10L,
                question = "Who are you?",
                answer = "I am an Android Engineer",
                memorizationLevel = MemorizationLevel.HIGH
            )
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
                    answer = UUID.randomUUID().toString(),
                    memorizationLevel = MemorizationLevel.HIGH
                )
            }, onCreateFlashCard = { _, _ -> }
        )
    }
}