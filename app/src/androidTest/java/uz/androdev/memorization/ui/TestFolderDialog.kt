package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import uz.androdev.memorization.R
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.ui.component.FolderDialog

/**
 * Created by: androdev
 * Date: 23-10-2022
 * Time: 10:43 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestFolderDialog {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val title = "Title"
    private val negativeButtonText = "NegativeButton"
    private val positiveButtonText = "PositiveButton"

    private val resources by lazy { composeRule.activity.resources }
    private val createFolderDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_dialog)) }
    private val createFolderDialogInputFieldMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_input_field)) }
    private val createFolderDialogPositiveButtonMatcher by lazy { hasText(positiveButtonText) }
    private val createFolderDialogNegativeButtonMatcher by lazy { hasText(negativeButtonText) }
    private val createFolderDialogTitleMatcher by lazy { hasText(title) }

    @Test
    fun testInitialState() {
        composeRule.setContent {
            FolderDialog(
                titleText = title,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = {}, onPositiveButtonClicked = {}
            )
        }

        composeRule.onNode(createFolderDialogMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogTitleMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogNegativeButtonMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogPositiveButtonMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogInputFieldMatcher).assertIsDisplayed()
    }

    @Test
    fun whenCreateClicked_ifTextIsBlankShouldPresentErrorAndShouldNotCallPositiveCallback() {
        val onDismissRequested: () -> Unit = mock()
        val onPositiveButtonClicked: (FolderInput) -> Unit = mock()

        composeRule.setContent {
            FolderDialog(
                titleText = title,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                onDismissRequested = {}, onPositiveButtonClicked = {}
            )
        }

        composeRule.onNode(createFolderDialogInputFieldMatcher).performTextInput("")
        composeRule.onNode(createFolderDialogPositiveButtonMatcher).performClick()

        Mockito.verifyNoInteractions(onDismissRequested)
        Mockito.verifyNoInteractions(onPositiveButtonClicked)
    }

    @Test
    fun dialog_shouldSetInitialFolderName() {
        val initialFolderName = "MyFolder"
        composeRule.setContent {
            FolderDialog(
                titleText = title,
                positiveButtonText = positiveButtonText,
                negativeButtonText = negativeButtonText,
                initialFolderName = initialFolderName,
                onDismissRequested = {}, onPositiveButtonClicked = {}
            )
        }

        composeRule.onNode(createFolderDialogInputFieldMatcher).assert(
            hasText(initialFolderName)
        )
    }
}