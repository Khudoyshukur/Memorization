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
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.component.FlashCardsListComponent

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 1:44 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestFlashCardListComponent {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val resources by lazy { composeRule.activity.resources }
    private val progressMatcher by lazy {
        hasTestTag(resources.getString(R.string.progress_bar))
    }
    private val flashCardListMatcher by lazy {
        hasTestTag(resources.getString(R.string.flash_cards_list))
    }
    private val noFlashCardsTextMatcher by lazy {
        hasText(resources.getString(R.string.no_flash_cards_message))
    }

    @Test
    fun flashCardList_whenFlashCardNull_shouldDisplayProgress() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = null
            )
        }

        composeRule.onNode(progressMatcher).assertIsDisplayed()
    }

    @Test
    fun flashCardList_whenCardNull_shouldNotDisplayFlashCardList() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = null
            )
        }

        composeRule.onNode(flashCardListMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenCardNull_shouldNotDisplayEmptyView() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = null
            )
        }

        composeRule.onNode(noFlashCardsTextMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenCardEmpty_shouldDisplayEmptyView() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = emptyList()
            )
        }

        composeRule.onNode(noFlashCardsTextMatcher).assertIsDisplayed()
    }

    @Test
    fun flashCardList_whenCardEmpty_shouldNotDisplayProgress() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = emptyList()
            )
        }

        composeRule.onNode(progressMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenCardEmpty_shouldNotDisplayCardList() {
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = emptyList()
            )
        }

        composeRule.onNode(flashCardListMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenCardNotEmpty_shouldDisplayCardList() {
        val flashCards = FlashCardFactory.createFlashCards(2)
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = flashCards
            )
        }

        composeRule.onNode(flashCardListMatcher).assertIsDisplayed()
    }

    @Test
    fun flashCardList_whenCardNotEmpty_shouldNotDisplayProgress() {
        val flashCards = FlashCardFactory.createFlashCards(2)
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = flashCards
            )
        }

        composeRule.onNode(progressMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenCardNotEmpty_shouldNotDisplayEmptyView() {
        val flashCards = FlashCardFactory.createFlashCards(2)
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = flashCards
            )
        }

        composeRule.onNode(noFlashCardsTextMatcher).assertDoesNotExist()
    }

    @Test
    fun flashCardList_whenFlashCardClicked_shouldCallOnClick() {
        val flashCards = FlashCardFactory.createFlashCards(5)
        val onFlashCardClicked: (FlashCard) -> Unit = mock()
        composeRule.setContent {
            FlashCardsListComponent(
                flashCards = flashCards,
                onFlashCardClicked = onFlashCardClicked
            )
        }

        val flashCard = flashCards.random()
        composeRule.onNode(flashCardListMatcher).performScrollToNode(
            hasText(flashCard.question)
        )
        composeRule.onNode(hasText(flashCard.question)).performClick()

        Mockito.verify(onFlashCardClicked).invoke(eq(flashCard))
    }
}