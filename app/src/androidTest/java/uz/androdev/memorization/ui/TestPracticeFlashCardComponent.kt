package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import uz.androdev.memorization.R
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.ui.component.PracticeFlashCardComponent

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 2:34 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestPracticeFlashCardComponent {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val getString: (resId: Int) -> String by lazy {
        { composeRule.activity.resources.getString(it) }
    }
    private val showButtonMatcher by lazy { hasText(getString(R.string.show_answer)) }
    private val hideButtonMatcher by lazy { hasText(getString(R.string.hide_answer)) }

    @Test
    fun component_shouldDisplayQuestionAndShowAnswerButtonInitially() {
        val flashCard = FlashCardFactory.createNewFlashCard()
        composeRule.setContent {
            PracticeFlashCardComponent(flashCard = flashCard)
        }

        composeRule.onNode(hasText(flashCard.question)).assertIsDisplayed()
        composeRule.onNode(showButtonMatcher).assertIsDisplayed()
    }

    @Test
    fun component_shouldNotDisplayAnswerAndHideAnswerButtonInitially() {
        val flashCard = FlashCardFactory.createNewFlashCard()
        composeRule.setContent {
            PracticeFlashCardComponent(flashCard = flashCard)
        }

        composeRule.onNode(hasText(flashCard.answer)).assertDoesNotExist()
        composeRule.onNode(hideButtonMatcher).assertDoesNotExist()
    }

    @Test
    fun component_whenShowButtonClicked_shouldShowAnswer() {
        val flashCard = FlashCardFactory.createNewFlashCard()
        composeRule.setContent {
            PracticeFlashCardComponent(flashCard = flashCard)
        }

        composeRule.onNode(showButtonMatcher).performClick()
        composeRule.onNode(hasText(flashCard.answer)).assertIsDisplayed()
    }

    @Test
    fun showButton_whenClicked_shouldChangeItsTextToHideButton() {
        val flashCard = FlashCardFactory.createNewFlashCard()
        composeRule.setContent {
            PracticeFlashCardComponent(flashCard = flashCard)
        }

        composeRule.onNode(showButtonMatcher).performClick()
        composeRule.onNode(hideButtonMatcher).assertIsDisplayed()
    }
}