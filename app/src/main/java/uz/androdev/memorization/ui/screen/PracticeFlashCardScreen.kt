package uz.androdev.memorization.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uz.androdev.memorization.R
import uz.androdev.memorization.domain.util.PracticeModelState
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.component.MessageWithActionComponent
import uz.androdev.memorization.ui.component.MemorizationLevelSelectorComponent
import uz.androdev.memorization.ui.component.NoItemsComponent
import uz.androdev.memorization.ui.component.PracticeFlashCardComponent
import uz.androdev.memorization.ui.theme.MemorizationTheme
import uz.androdev.memorization.ui.util.FlashCardFactory
import uz.androdev.memorization.ui.viewmodel.PracticeFlashCardsScreenAction
import uz.androdev.memorization.ui.viewmodel.PracticeFlashCardsScreenViewModel

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 12:33 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun PracticeFlashCardScreenRoute(
    viewModel: PracticeFlashCardsScreenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val practiceModelState by viewModel.practiceModelState.collectAsState()
    PracticeFlashCardScreen(
        practiceModelState = practiceModelState,
        onMemorizationLevelSelected = {
            viewModel.processAction(PracticeFlashCardsScreenAction.Next(it))
        },
        onExitPractice = onNavigateBack
    )
}

@Composable
fun PracticeFlashCardScreen(
    modifier: Modifier = Modifier,
    practiceModelState: PracticeModelState,
    onMemorizationLevelSelected: (MemorizationLevel) -> Unit,
    onExitPractice: () -> Unit
) {
    when (practiceModelState) {
        PracticeModelState.EmptyFlashCards -> {
            EmptyFlashCardsPage(modifier = modifier, onExitPractice = onExitPractice)
        }
        PracticeModelState.FailedToInitialize -> {
            InitializationErrorPage(modifier = modifier, onExitPractice = onExitPractice)
        }
        PracticeModelState.Finished -> {
            FinishedPracticePage(modifier = modifier, onExitPractice = onExitPractice)
        }
        PracticeModelState.Initializing, PracticeModelState.Idle -> {
            LoadingPage(modifier = modifier)
        }
        is PracticeModelState.Practice -> {
            Crossfade(targetState = practiceModelState.flashCard) {
                PracticePage(
                    modifier = modifier,
                    flashCard = it,
                    onMemorizationLevelSelected = onMemorizationLevelSelected
                )
            }
        }
    }
}

@Composable
fun PracticePage(
    modifier: Modifier = Modifier,
    flashCard: FlashCard,
    onMemorizationLevelSelected: (MemorizationLevel) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.weight(1f)) {
            PracticeFlashCardComponent(
                modifier = Modifier.padding(16.dp)
                    .align(Alignment.Center),
                flashCard = flashCard
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.what_is_your_memorization_level),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            MemorizationLevelSelectorComponent(
                onMemorizationLevelSelected = onMemorizationLevelSelected
            )
        }
    }
}

@Composable
fun InitializationErrorPage(modifier: Modifier = Modifier, onExitPractice: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        MessageWithActionComponent(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(id = R.string.failed_to_initialize_practice),
            actionButtonText = stringResource(id = R.string.go_back),
            onActionInvoked = onExitPractice
        )
    }
}

@Composable
fun EmptyFlashCardsPage(modifier: Modifier = Modifier, onExitPractice: () -> Unit) {
    Box(modifier = modifier.fillMaxSize()) {
        MessageWithActionComponent(
            modifier = Modifier.align(Alignment.Center),
            message = stringResource(id = R.string.no_flash_cards_for_practice),
            actionButtonText = stringResource(id = R.string.go_back),
            onActionInvoked = onExitPractice
        )
    }
}

@Composable
fun LoadingPage(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
                .align(Alignment.Center)
                .testTag(stringResource(id = R.string.progress_bar)),
        )
    }
}

@Composable
fun FinishedPracticePage(
    modifier: Modifier = Modifier,
    onExitPractice: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier.align(Alignment.Center)) {
            NoItemsComponent(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp),
                message = stringResource(id = R.string.practice_finished_message)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                onClick = onExitPractice
            ) {
                Text(text = stringResource(id = R.string.go_back))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PracticeFlashCardPage_PracticePreview() {
    MemorizationTheme {
        PracticeFlashCardScreen(
            practiceModelState = PracticeModelState.Practice(FlashCardFactory.createFlashCard()),
            onMemorizationLevelSelected = {},
            onExitPractice = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PracticeFlashCardPage_ErrorPreview() {
    MemorizationTheme {
        PracticeFlashCardScreen(
            practiceModelState = PracticeModelState.FailedToInitialize,
            onMemorizationLevelSelected = {},
            onExitPractice = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PracticeFlashCardPage_EmptyPreview() {
    MemorizationTheme {
        PracticeFlashCardScreen(
            practiceModelState = PracticeModelState.EmptyFlashCards,
            onMemorizationLevelSelected = {},
            onExitPractice = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PracticeFlashCardPage_InitializingPreview() {
    MemorizationTheme {
        PracticeFlashCardScreen(
            practiceModelState = PracticeModelState.Initializing,
            onMemorizationLevelSelected = {},
            onExitPractice = {}
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun PracticeFlashCardPage_FinishedPreview() {
    MemorizationTheme {
        PracticeFlashCardScreen(
            practiceModelState = PracticeModelState.Finished,
            onMemorizationLevelSelected = {},
            onExitPractice = {}
        )
    }
}