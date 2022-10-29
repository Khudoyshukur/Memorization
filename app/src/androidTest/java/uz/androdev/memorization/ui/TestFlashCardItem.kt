package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.model.model.FlashCard
import uz.androdev.memorization.ui.component.FlashCardItem

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 12:18 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestFlashCardItem {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun flashCardShouldDisplayQuestionAndItsIndex() {
        val flashCard = FlashCardFactory.createNewFlashCard()
        val orderNumber = 10
        composeRule.setContent {
            FlashCardItem(
                flashCardOrderNumber = orderNumber, flashCard = flashCard
            )
        }

        composeRule.onNodeWithText(orderNumber.toString()).assertIsDisplayed()
        composeRule.onNodeWithText(flashCard.question).assertIsDisplayed()
    }

    @Test
    fun flashCard_whenClicked_shouldCallOnClick() {
        val onFlashCardClicked: (FlashCard) -> Unit = mock()
        val flashCard = FlashCardFactory.createNewFlashCard()
        val orderNumber = 10

        val testTag = "FlashCardItem"

        composeRule.setContent {
            FlashCardItem(
                modifier = Modifier.testTag(testTag),
                flashCardOrderNumber = orderNumber,
                flashCard = flashCard,
                onFlashCardClicked = onFlashCardClicked
            )
        }

        composeRule.onNodeWithTag(testTag).performClick()

        Mockito.verify(onFlashCardClicked).invoke(eq(flashCard))
    }
}