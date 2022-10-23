package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import uz.androdev.memorization.R
import uz.androdev.memorization.data.db.dao.FolderDao
import uz.androdev.memorization.di.FoldersDaoModule
import uz.androdev.memorization.domain.usecase.CreateFolderUseCase
import uz.androdev.memorization.domain.usecase.GetFoldersUseCase
import uz.androdev.memorization.fake.FakeFolderDao
import uz.androdev.memorization.ui.screen.FolderScreenRoute
import uz.androdev.memorization.ui.viewmodel.FoldersScreenViewModel
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 5:16 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
@UninstallModules(FoldersDaoModule::class)
@HiltAndroidTest
class TestFoldersScreenRoute {
    private val composeRule = createAndroidComposeRule<ComponentActivity>()
    private val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(hiltRule)
        .around(composeRule)

    private val resources by lazy { composeRule.activity.resources }
    private val progressBarMatcher by lazy { hasTestTag(resources.getString(R.string.progress_bar)) }
    private val noItemsComponentMatcher by lazy { hasContentDescription(resources.getString(R.string.empty_folder)) }
    private val foldersLazyColumnMatcher by lazy { hasTestTag(resources.getString(R.string.folders_list)) }
    private val createFolderDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_dialog)) }
    private val createFolderDialogInputFieldMatcher by lazy { hasTestTag(resources.getString(R.string.create_folder_input_field)) }
    private val createFolderDialogCreateButtonMatcher by lazy { hasText(resources.getString(R.string.create)) }
    private val addButtonMatcher by lazy { hasContentDescription(resources.getString(R.string.add_folder)) }

    @Inject
    lateinit var getFoldersUseCase: GetFoldersUseCase

    @Inject
    lateinit var createFoldersUseCase: CreateFolderUseCase

    @BindValue
    @JvmField
    val folderDao: FolderDao = FakeFolderDao()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: FoldersScreenViewModel

    @Before
    fun setUp() {
        hiltRule.inject()

        viewModel = FoldersScreenViewModel(getFoldersUseCase, createFoldersUseCase)
    }

    @Test
    fun testInitialState() {
        composeRule.setContent {
            FolderScreenRoute(onNavigateToItemsScreen = {}, viewModel = viewModel)
        }

        composeRule.onNode(noItemsComponentMatcher).assertIsDisplayed()
        composeRule.onNode(addButtonMatcher).assertIsDisplayed()
        composeRule.onNode(foldersLazyColumnMatcher).assertDoesNotExist()
        composeRule.onNode(foldersLazyColumnMatcher).assertDoesNotExist()
    }

    @Test
    fun whenAddButtonClicked_shouldShowAddFolderDialog() {
        composeRule.setContent {
            FolderScreenRoute(onNavigateToItemsScreen = {}, viewModel = viewModel)
        }

        composeRule.onNode(addButtonMatcher).performClick()
        composeRule.onNode(createFolderDialogMatcher).assertIsDisplayed()
    }

    @Test
    fun createFolder_createsTheFolderInTheDataSource() = runTest {
        composeRule.setContent {
            FolderScreenRoute(onNavigateToItemsScreen = {}, viewModel = viewModel)
        }

        val newFolderTitle = "My new folder"

        composeRule.onNode(addButtonMatcher).performClick()
        composeRule.onNode(createFolderDialogInputFieldMatcher).performTextInput(newFolderTitle)
        composeRule.onNode(createFolderDialogCreateButtonMatcher).performClick()

        composeRule.waitUntilExists(
            hasText(newFolderTitle),
        )

        val foldersInTheDao = folderDao.getFolders().first()
        assertTrue(foldersInTheDao.count { it.title == newFolderTitle } == 1)
    }
}