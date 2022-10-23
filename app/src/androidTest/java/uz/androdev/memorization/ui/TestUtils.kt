package uz.androdev.memorization.ui

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.junit4.ComposeContentTestRule

/**
 * Created by: androdev
 * Date: 23-10-2022
 * Time: 10:44 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

fun ComposeContentTestRule.waitUntilNodeCount(
    matcher: SemanticsMatcher,
    count: Int,
    timeoutMillis: Long = 1_000L
) {
    this.waitUntil(timeoutMillis) {
        this.onAllNodes(matcher).fetchSemanticsNodes().size == count
    }
}

fun ComposeContentTestRule.waitUntilExists(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 1, timeoutMillis)
}

fun ComposeContentTestRule.waitUntilDoesNotExist(
    matcher: SemanticsMatcher,
    timeoutMillis: Long = 1_000L
) {
    return this.waitUntilNodeCount(matcher, 0, timeoutMillis)
}