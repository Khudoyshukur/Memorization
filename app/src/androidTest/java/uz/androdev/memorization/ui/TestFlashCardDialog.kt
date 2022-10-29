package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import uz.androdev.memorization.R
import uz.androdev.memorization.ui.component.FlashCardDialog
import java.util.UUID

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 2:07 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestFlashCardDialog {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val resources by lazy { composeRule.activity.resources }
    private val titleText = "Title"
    private val positiveButtonText = "Positive"
    private val negativeButtonText = "Negative"
    private val createFlashCardDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_flash_card_dialog)) }
    private val createFlashCardDialogQuestionInputFieldMatcher by lazy {
        hasTestTag(resources.getString(R.string.question_input_field))
    }
    private val createFlashCardDialogAnswerInputFieldMatcher by lazy {
        hasTestTag(resources.getString(R.string.answer_input_field))
    }
    private val createFlashCardDialogCreateButtonMatcher by lazy { hasText(positiveButtonText) }
    private val createFlashCardDialogCancelButtonMatcher by lazy { hasText(negativeButtonText) }
    private val createFlashCardDialogTitleMatcher by lazy { hasText(titleText) }

    @Test
    fun testInitialState() {
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = { }, onSubmit = { _, _ -> }
            )
        }

        composeRule.onNode(createFlashCardDialogMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogTitleMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogQuestionInputFieldMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogAnswerInputFieldMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogCancelButtonMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher).assertIsDisplayed()
    }

    @Test
    fun whenCancelClicked_shouldRequestDismissal() {
        val onDismissRequested: () -> Unit = mock()
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = onDismissRequested, onSubmit = { _, _ -> }
            )
        }

        composeRule.onNode(createFlashCardDialogCancelButtonMatcher).performClick()

        Mockito.verify(onDismissRequested).invoke()
    }

    @Test
    fun whenCreateClicked_ifAnswerBlank_shouldNotCallCreateOrDismiss() {
        val onDismissRequested: () -> Unit = mock()
        val createFlashCard: (String, String) -> Unit = mock()
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = onDismissRequested, onSubmit = createFlashCard
            )
        }

        val questionText = "Who are you?"
        composeRule.onNode(createFlashCardDialogQuestionInputFieldMatcher)
            .performTextInput(questionText)
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher)
            .performClick()

        Mockito.verifyNoInteractions(onDismissRequested)
        Mockito.verifyNoInteractions(createFlashCard)
    }

    @Test
    fun whenCreateClicked_ifQuestionBlank_shouldNotCallCreateOrDismiss() {
        val onDismissRequested: () -> Unit = mock()
        val createFlashCard: (String, String) -> Unit = mock()
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = onDismissRequested, onSubmit = createFlashCard
            )
        }

        val answerText = "My answer here"
        composeRule.onNode(createFlashCardDialogAnswerInputFieldMatcher)
            .performTextInput(answerText)
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher)
            .performClick()

        Mockito.verifyNoInteractions(onDismissRequested)
        Mockito.verifyNoInteractions(createFlashCard)
    }

    @Test
    fun whenCreateClicked_ifQuestionAndAnswerNotBlank_shouldRequestCreation() {
        val onDismissRequested: () -> Unit = mock()
        val createFlashCard: (String, String) -> Unit = mock()
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = onDismissRequested, onSubmit = createFlashCard
            )
        }

        val questionText = "Who are you?"
        val answerText = "I am an Android Engineer"
        composeRule.onNode(createFlashCardDialogQuestionInputFieldMatcher)
            .performTextInput(questionText)
        composeRule.onNode(createFlashCardDialogAnswerInputFieldMatcher)
            .performTextInput(answerText)
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher)
            .performClick()

        Mockito.verify(createFlashCard).invoke(eq(questionText), eq(answerText))
    }

    @Test
    fun whenInitialTextsProvided_shouldDisplayThem() {
        val initialQuestion = UUID.randomUUID().toString()
        val initialAnswer = UUID.randomUUID().toString()
        composeRule.setContent {
            FlashCardDialog(
                title = titleText,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                initialQuestionText = initialQuestion,
                initialAnswerText = initialAnswer,
                onDismissRequested = { }, onSubmit = { _, _ -> }
            )
        }

        composeRule.onNode(createFlashCardDialogMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogTitleMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogQuestionInputFieldMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogAnswerInputFieldMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogCancelButtonMatcher).assertIsDisplayed()
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher).assertIsDisplayed()

        composeRule.onNode(createFlashCardDialogQuestionInputFieldMatcher)
            .assert(hasText(initialQuestion))
        composeRule.onNode(createFlashCardDialogAnswerInputFieldMatcher)
            .assert(hasText(initialAnswer))
    }
}