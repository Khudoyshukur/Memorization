package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import uz.androdev.memorization.R
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.di.FlashCardDaoModule
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.GetFlashCardsUseCase
import uz.androdev.memorization.fake.FakeFlashCardDao
import uz.androdev.memorization.ui.navigation.Arguments
import uz.androdev.memorization.ui.screen.FlashCardScreenRoute
import uz.androdev.memorization.ui.viewmodel.FlashCardsScreenViewModel
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 2:54 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
@UninstallModules(FlashCardDaoModule::class)
@HiltAndroidTest
class TestFlashCardScreenRoute {
    private val composeRule = createAndroidComposeRule<ComponentActivity>()
    private val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val chain: RuleChain = RuleChain.outerRule(hiltRule)
        .around(composeRule)

    @Inject
    lateinit var getFlashCardsUseCase: GetFlashCardsUseCase

    @Inject
    lateinit var createFlashCardUseCase: CreateFlashCardUseCase

    @BindValue
    @JvmField
    val flashCardDao: FlashCardDao = FakeFlashCardDao()

    private lateinit var savedStateHandle: SavedStateHandle

    private val folderId = 10L
    private lateinit var viewModel: FlashCardsScreenViewModel

    private val resources by lazy { composeRule.activity.resources }
    private val progressBarMatcher by lazy { hasTestTag(resources.getString(R.string.progress_bar)) }
    private val noFlashCardsTextMatcher by lazy { hasText(resources.getString(R.string.no_flash_cards_message)) }
    private val flashCardsLazyColumnMatcher by lazy { hasTestTag(resources.getString(R.string.flash_cards_list)) }
    private val createFlashCardDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_flash_card_dialog)) }
    private val createFlashCardDialogQuestionFieldMatcher by lazy { hasTestTag(resources.getString(R.string.question_input_field)) }
    private val createFlashCardDialogAnswerFieldMatcher by lazy { hasTestTag(resources.getString(R.string.answer_input_field)) }
    private val createFlashCardDialogCreateButtonMatcher by lazy { hasText(resources.getString(R.string.create)) }
    private val addButtonMatcher by lazy { hasContentDescription(resources.getString(R.string.add_flash_card)) }

    @Before
    fun setUp() {
        hiltRule.inject()

        savedStateHandle = SavedStateHandle(
            mapOf(Arguments.ARGUMENT_FOLDER_ID to folderId)
        )

        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase = getFlashCardsUseCase,
            createFlashCardUseCase = createFlashCardUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun testInitialState() {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        composeRule.onNode(noFlashCardsTextMatcher).assertIsDisplayed()
        composeRule.onNode(addButtonMatcher).assertIsDisplayed()
        composeRule.onNode(flashCardsLazyColumnMatcher).assertDoesNotExist()
    }

    @Test
    fun whenAddButtonClicked_shouldShowCreateFlashCardDialog() {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        composeRule.onNode(addButtonMatcher).performClick()
        composeRule.onNode(createFlashCardDialogMatcher).assertIsDisplayed()
    }

    @Test
    fun createFolder_createsTheFolderInTheDataSource() = runTest {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        val questionText = "Who are you?"
        val answerText = "I am an Android Engineer"

        composeRule.onNode(addButtonMatcher).performClick()
        composeRule.onNode(createFlashCardDialogQuestionFieldMatcher).performTextInput(questionText)
        composeRule.onNode(createFlashCardDialogAnswerFieldMatcher).performTextInput(answerText)
        composeRule.onNode(createFlashCardDialogCreateButtonMatcher).performClick()

        composeRule.waitUntilExists(
            hasText(questionText)
        )

        val foldersInTheDao = flashCardDao.getAllFlashCards(folderId).first()
        Assert.assertTrue(
            foldersInTheDao.count {
                it.question == questionText && it.answer == answerText
            } == 1
        )
    }
}