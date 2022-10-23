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
import uz.androdev.memorization.ui.component.CreateFolderDialog

/**
 * Created by: androdev
 * Date: 23-10-2022
 * Time: 10:43 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestCreateFolderDialog {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val resources by lazy { composeRule.activity.resources }
    private val createFolderDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_dialog)) }
    private val createFolderDialogInputFieldMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_input_field)) }
    private val createFolderDialogCreateButtonMatcher by lazy { hasText(resources.getString(R.string.create)) }
    private val createFolderDialogCancelButtonMatcher by lazy { hasText(resources.getString(R.string.cancel)) }
    private val createFolderDialogTitleMatcher by lazy { hasText(resources.getString(R.string.create_folder)) }

    @Test
    fun testInitialState() {
        composeRule.setContent {
            CreateFolderDialog(onDismissRequested = {}, onCreateFolder = {})
        }

        composeRule.onNode(createFolderDialogMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogTitleMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogCancelButtonMatcher).assertIsDisplayed()
        composeRule.onNode(createFolderDialogInputFieldMatcher).assertIsDisplayed()
    }

    @Test
    fun whenCreateClicked_ifTextIsBlankShouldPresentErrorAndShouldNotCallCreate() {
        val onDismissRequested: () -> Unit = mock()
        val onCreateFolder: (FolderInput) -> Unit = mock()

        composeRule.setContent {
            CreateFolderDialog(
                onDismissRequested = onDismissRequested,
                onCreateFolder = onCreateFolder
            )
        }

        composeRule.onNode(createFolderDialogInputFieldMatcher).performTextInput("")
        composeRule.onNode(createFolderDialogCreateButtonMatcher).performClick()

        Mockito.verifyNoInteractions(onDismissRequested)
        Mockito.verifyNoInteractions(onCreateFolder)
    }
}