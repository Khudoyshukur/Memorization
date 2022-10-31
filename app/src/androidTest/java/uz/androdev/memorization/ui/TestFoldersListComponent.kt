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
import uz.androdev.memorization.model.model.Folder
import uz.androdev.memorization.ui.component.FoldersListComponent

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 12:07 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestFoldersListComponent {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val resources by lazy { composeTestRule.activity.resources }
    private val progressBarMatcher by lazy { hasTestTag(resources.getString(R.string.progress_bar)) }
    private val noItemsComponentMatcher by lazy { hasContentDescription(resources.getString(R.string.empty_folder)) }
    private val foldersLazyColumnMatcher by lazy { hasTestTag(resources.getString(R.string.folders_list)) }
    private val folderOptionsButtonMatcher by lazy { hasContentDescription(resources.getString(R.string.options_menu_icon)) }
    private val updateOptionMatcher by lazy { hasText(resources.getString(R.string.edit)) }
    private val removeOptionMatcher by lazy { hasText(resources.getString(R.string.remove)) }

    @Test
    fun whenListIsNull_shouldShowLoading() {
        composeTestRule.setContent { FoldersListComponent(folders = null) }

        composeTestRule.onNode(progressBarMatcher).assertIsDisplayed()
    }

    @Test
    fun whenListIsEmpty_shouldNotDisplayLoading() {
        composeTestRule.setContent { FoldersListComponent(folders = emptyList()) }
        composeTestRule.onNode(progressBarMatcher).assertDoesNotExist()
    }

    @Test
    fun whenListIsNotEmpty_shouldNotDisplayLoading() {
        composeTestRule.setContent { FoldersListComponent(folders = listOf(Folder(1, "title"))) }
        composeTestRule.onNode(progressBarMatcher).assertDoesNotExist()
    }

    @Test
    fun whenListIsEmpty_shouldShowEmptyImage() {
        composeTestRule.setContent { FoldersListComponent(folders = emptyList()) }
        composeTestRule.onNode(noItemsComponentMatcher).assertIsDisplayed()
    }

    @Test
    fun whenListIsNotEmpty_shouldNotShowEmptyImage() {
        composeTestRule.setContent { FoldersListComponent(folders = listOf(Folder(1, "Folder"))) }
        composeTestRule.onNode(noItemsComponentMatcher).assertDoesNotExist()
    }

    @Test
    fun whenListIsNull_shouldNotShowEmptyImage() {
        composeTestRule.setContent { FoldersListComponent(folders = null) }
        composeTestRule.onNode(noItemsComponentMatcher).assertDoesNotExist()
    }

    @Test
    fun whenListIsNotEmpty_shouldShowFolders() {
        val folders = List(5) {
            Folder(it.toLong(), "Folder$it")
        }
        composeTestRule.setContent { FoldersListComponent(folders = folders) }

        folders.forEach {
            composeTestRule.onNode(foldersLazyColumnMatcher).performScrollToNode(
                hasText(it.title)
            )
            composeTestRule.onNode(hasText(it.title)).assertIsDisplayed()
        }
    }

    @Test
    fun whenListIsEmptyOrNull_shouldNotShowFolders() {
        composeTestRule.setContent { FoldersListComponent(folders = emptyList()) }
        composeTestRule.onNode(foldersLazyColumnMatcher).assertDoesNotExist()
    }

    @Test
    fun whenListIsNull_shouldNotShowFolders() {
        composeTestRule.setContent { FoldersListComponent(folders = null) }
        composeTestRule.onNode(foldersLazyColumnMatcher).assertDoesNotExist()
    }

    @Test
    fun whenFolderClicked_shouldInvokeOnClickCallback() {
        val onFolderClicked: (Folder) -> Unit = mock()
        val folders = List(5) {
            Folder(it.toLong(), "Folder$it")
        }
        composeTestRule.setContent {
            FoldersListComponent(
                folders = folders,
                onFolderClicked = onFolderClicked
            )
        }

        val randomFolder = folders.random()
        composeTestRule.onNode(foldersLazyColumnMatcher).performScrollToNode(
            hasText(randomFolder.title)
        )
        composeTestRule.onNode(hasText(randomFolder.title)).performClick()

        Mockito.verify(onFolderClicked).invoke(eq(randomFolder))
    }

    @Test
    fun removeFolderAction_shouldInvokeRemoveFolderCallback() {
        val onRemoveFolder: (Folder) -> Unit = mock()
        val folders = List(5) {
            Folder(it.toLong(), "Folder$it")
        }
        composeTestRule.setContent {
            FoldersListComponent(
                folders = folders,
                onRemoveFolded = onRemoveFolder
            )
        }

        val indexOfRandomFolder = folders.indices.random()
        val randomFolder = folders[indexOfRandomFolder]

        composeTestRule.onNode(foldersLazyColumnMatcher).performScrollToNode(
            hasText(randomFolder.title)
        )
        composeTestRule.onAllNodes(folderOptionsButtonMatcher)[indexOfRandomFolder].performClick()
        composeTestRule.onNode(removeOptionMatcher).performClick()

        Mockito.verify(onRemoveFolder).invoke(eq(randomFolder))
    }

    @Test
    fun updateFolderAction_shouldInvokeUpdateFolderCallback() {
        val onUpdateFolder: (Folder) -> Unit = mock()
        val folders = List(5) {
            Folder(it.toLong(), "Folder$it")
        }
        composeTestRule.setContent {
            FoldersListComponent(
                folders = folders,
                onUpdateFolded = onUpdateFolder
            )
        }

        val indexOfRandomFolder = folders.indices.random()
        val randomFolder = folders[indexOfRandomFolder]

        composeTestRule.onNode(foldersLazyColumnMatcher).performScrollToNode(
            hasText(randomFolder.title)
        )
        composeTestRule.onAllNodes(folderOptionsButtonMatcher)[indexOfRandomFolder].performClick()
        composeTestRule.onNode(updateOptionMatcher).performClick()

        Mockito.verify(onUpdateFolder).invoke(eq(randomFolder))
    }
}