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
import uz.androdev.memorization.domain.usecase.RemoveFlashCardUseCase
import uz.androdev.memorization.domain.usecase.UpdateFlashCardUseCase
import uz.androdev.memorization.fake.FakeFlashCardDao
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.ui.navigation.Arguments
import uz.androdev.memorization.ui.screen.FlashCardScreenRoute
import uz.androdev.memorization.ui.viewmodel.FlashCardScreenAction
import uz.androdev.memorization.ui.viewmodel.FlashCardsScreenViewModel
import java.util.*
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

    @Inject
    lateinit var removeFlashCardUseCase: RemoveFlashCardUseCase

    @Inject
    lateinit var updateFlashCardUseCase: UpdateFlashCardUseCase

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
    private val flashCardDialogMatcher by lazy { hasTestTag(resources.getString(R.string.create_flash_card_dialog)) }
    private val createFlashCardDialogQuestionFieldMatcher by lazy { hasTestTag(resources.getString(R.string.question_input_field)) }
    private val createFlashCardDialogAnswerFieldMatcher by lazy { hasTestTag(resources.getString(R.string.answer_input_field)) }
    private val createFlashCardDialogCreateButtonMatcher by lazy { hasText(resources.getString(R.string.create)) }
    private val addButtonMatcher by lazy { hasContentDescription(resources.getString(R.string.add_flash_card)) }
    private val flashCardDetailsSheetMatcher by lazy { hasTestTag(resources.getString(R.string.flash_card_details_sheet)) }
    private val editFlashCardButtonMatcher by lazy { hasText(resources.getString(R.string.edit)) }
    private val removeFlashCardButtonMatcher by lazy { hasText(resources.getString(R.string.remove)) }
    private val editFlashCardDialogPositiveButtonMatcher by lazy { hasText(resources.getString(R.string.edit)) }

    @Before
    fun setUp() {
        hiltRule.inject()

        savedStateHandle = SavedStateHandle(
            mapOf(Arguments.ARGUMENT_FOLDER_ID to folderId)
        )

        viewModel = FlashCardsScreenViewModel(
            getFlashCardsUseCase = getFlashCardsUseCase,
            createFlashCardUseCase = createFlashCardUseCase,
            removeFlashCardUseCase = removeFlashCardUseCase,
            updateFlashCardUseCase = updateFlashCardUseCase,
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
        composeRule.onNode(flashCardDialogMatcher).assertIsDisplayed()
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

    @Test
    fun whenClickedOnFlashCard_shouldShowDetailsInBottomSheet() = runTest {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        // create flash card
        val question = "Who are you?"
        val answer = "I am an Android Engineer"
        val action = FlashCardScreenAction.CreateFlashCard(question = question, answer = answer)
        viewModel.processAction(action)

        // wait until item appears in the list
        composeRule.waitUntilExists(
            hasText(question)
        )

        // click the flash card item
        composeRule.onNode(hasText(question)).performClick()

        // check if bottom sheet dialog is displayed
        composeRule.onNode(flashCardDetailsSheetMatcher).assertIsDisplayed()
        composeRule.onAllNodesWithText(question)[0].assertIsDisplayed()
        composeRule.onAllNodesWithText(question)[1].assertIsDisplayed()
        composeRule.onNode(hasText(answer)).assertIsDisplayed()
        composeRule.onNode(removeFlashCardButtonMatcher).assertIsDisplayed()
        composeRule.onNode(editFlashCardButtonMatcher).assertIsDisplayed()
    }

    @Test
    fun whenRemoveClickedInBottomSheet_shouldDismissBottomSheet_shouldRemoveFlashCard() = runTest {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        val entities = List(3) {
            FlashCardEntity(
                folderId = folderId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString()
            ).also {
                flashCardDao.insertFlashCard(it)
            }
        }

        composeRule.waitUntilExists(
            hasText(entities.first().question)
        )

        val flashCards = viewModel.uiState.value.flashCards!!
        val randomFlashCard = flashCards.random()
        // click the flash card item
        composeRule.onNode(hasText(randomFlashCard.question)).performClick()

        // click remove flash card button
        composeRule.onNode(removeFlashCardButtonMatcher).performClick()

        composeRule.waitUntilDoesNotExist(flashCardDetailsSheetMatcher, 2000)
        composeRule.waitUntilDoesNotExist(hasText(randomFlashCard.question), 2000)

        // check whether other flash cards exist
        flashCards.filter { it != randomFlashCard }.forEach {
            composeRule.onNode(hasText(it.question)).assertIsDisplayed()
        }
    }

    @Test
    fun whenEditClickedInBottomSheet_shouldDismissBottomSheet_shouldEditFlashCard() = runTest {
        composeRule.setContent {
            FlashCardScreenRoute(viewModel = viewModel)
        }

        val entities = List(3) {
            FlashCardEntity(
                folderId = folderId,
                question = UUID.randomUUID().toString(),
                answer = UUID.randomUUID().toString()
            ).also {
                flashCardDao.insertFlashCard(it)
            }
        }

        composeRule.waitUntilExists(
            hasText(entities.first().question)
        )

        val flashCards = viewModel.uiState.value.flashCards!!
        val randomFlashCard = flashCards.random()
        // click the flash card item
        composeRule.onNode(hasText(randomFlashCard.question)).performClick()

        // click remove flash card button
        composeRule.onNode(editFlashCardButtonMatcher).performClick()

        composeRule.waitUntilDoesNotExist(flashCardDetailsSheetMatcher)

        composeRule.onNode(flashCardDialogMatcher).assertIsDisplayed()

        // check input fields
        composeRule.onNode(createFlashCardDialogQuestionFieldMatcher)
            .assert(hasText(randomFlashCard.question))
        composeRule.onNode(createFlashCardDialogAnswerFieldMatcher)
            .assert(hasText(randomFlashCard.answer))

        val questionText = "Who are you?"
        val answerText = "I am an Android Engineer"
        composeRule.onNode(createFlashCardDialogQuestionFieldMatcher).performTextClearance()
        composeRule.onNode(createFlashCardDialogQuestionFieldMatcher).performTextInput(questionText)

        composeRule.onNode(createFlashCardDialogAnswerFieldMatcher).performTextClearance()
        composeRule.onNode(createFlashCardDialogAnswerFieldMatcher).performTextInput(answerText)

        composeRule.onNode(editFlashCardDialogPositiveButtonMatcher).performClick()

        // check whether randomFlashCard has been edited
        composeRule.waitUntilDoesNotExist(hasText(randomFlashCard.question))
        composeRule.waitUntilExists(hasText(questionText))

        // check whether other flash cards exist
        flashCards.filter { it != randomFlashCard }.forEach {
            composeRule.onNode(hasText(it.question)).assertIsDisplayed()
        }
    }
}