package uz.androdev.memorization.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import uz.androdev.memorization.R
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.ui.component.MemorizationLevelSelectorComponent

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 3:12 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class TestMemorizationLevelSelectorComponent {
    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    private val rs by lazy {
        composeRule.activity.resources
    }
    private val lowMatcher by lazy { hasText(rs.getString(R.string.low)) }
    private val mediumMatcher by lazy { hasText(rs.getString(R.string.medium)) }
    private val highMatcher by lazy { hasText(rs.getString(R.string.high)) }

    @Test
    fun allLevelButtons_shouldBeDisplayed() {
        composeRule.setContent {
            MemorizationLevelSelectorComponent(onMemorizationLevelSelected = {})
        }

        composeRule.onNode(lowMatcher).assertIsDisplayed()
        composeRule.onNode(mediumMatcher).assertIsDisplayed()
        composeRule.onNode(highMatcher).assertIsDisplayed()
    }

    @Test
    fun component_whenButtonsClicked_shouldInvokeCallbackWithAppropriateMemorizationLevel() {
        val callback: (MemorizationLevel) -> Unit = mock()
        composeRule.setContent {
            MemorizationLevelSelectorComponent(onMemorizationLevelSelected = callback)
        }

        composeRule.onNode(lowMatcher).performClick()
        Mockito.verify(callback).invoke(eq(MemorizationLevel.LOW))

        composeRule.onNode(mediumMatcher).performClick()
        Mockito.verify(callback).invoke(eq(MemorizationLevel.MEDIUM))

        composeRule.onNode(highMatcher).performClick()
        Mockito.verify(callback).invoke(eq(MemorizationLevel.HIGH))
    }
}