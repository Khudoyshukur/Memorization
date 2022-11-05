package uz.androdev.memorization.data.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import uz.androdev.memorization.factory.FlashCardFactory
import uz.androdev.memorization.model.enums.MemorizationLevel

/**
 * Created by: androdev
 * Date: 01-11-2022
 * Time: 5:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@OptIn(ExperimentalCoroutinesApi::class)
class TestPracticeFlashCardSelector {

//    @Test(expected = IllegalArgumentException::class)
//    fun getFlashCardsToPractice_whenIllegalMemorizationLevelPercentagesGiven_shouldThrowException() =
//        runTest {
//            val selector = PracticeFlashCardSelector(
//                flashCards = emptyList(),
//                sizeLimit = 10,
//                memorizationLevelPercentages = mapOf()
//            )
//            selector.select()
//        }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun getFlashCardsToPractice_whenZeroSizeGivenGiven_shouldThrowException() = runTest {
//        val selector = PracticeFlashCardSelector(
//            flashCards = emptyList(),
//            sizeLimit = 0,
//            memorizationLevelPercentages = mapOf()
//        )
//        selector.select()
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun getFlashCardsToPractice_whenNegativeSizeGivenGiven_shouldThrowException() = runTest {
//        val selector = PracticeFlashCardSelector(
//            flashCards = emptyList(),
//            sizeLimit = -5,
//            memorizationLevelPercentages = mapOf()
//        )
//        selector.select()
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun whenMemorizationPercentageIsNotWithinRange_shouldReturnException() = runTest {
//        val selector = PracticeFlashCardSelector(
//            flashCards = emptyList(),
//            sizeLimit = 10,
//            memorizationLevelPercentages = mapOf(
//                MemorizationLevel.LOW to -1,
//                MemorizationLevel.HIGH to 101
//            )
//        )
//        selector.select()
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun providedList_shouldNotContainFlashCard_whichHasMemorizationLevelNoPercent() = runTest {
//        val memorizationLevel = MemorizationLevel.MEDIUM
//        val selector = PracticeFlashCardSelector(
//            flashCards = listOf(
//                FlashCardFactory.createNewFlashCard().copy(
//                    memorizationLevel = memorizationLevel
//                )
//            ),
//            sizeLimit = 5,
//            memorizationLevelPercentages = mapOf(
//                MemorizationLevel.LOW to 100
//            )
//        )
//        selector.select()
//    }
//
//    @Test(expected = IllegalArgumentException::class)
//    fun providedList_shouldNotContainFlashCard_whichHasMemorizationLevelZeroPercent() = runTest {
//        val memorizationLevel = MemorizationLevel.MEDIUM
//        val selector = PracticeFlashCardSelector(
//            flashCards = listOf(
//                FlashCardFactory.createNewFlashCard().copy(
//                    memorizationLevel = memorizationLevel
//                )
//            ),
//            sizeLimit = 5,
//            memorizationLevelPercentages = mapOf(
//                memorizationLevel to 0,
//                MemorizationLevel.LOW to 100
//            )
//        )
//        selector.select()
//    }
//
//    @Test
//    fun whenOneMemorizationLevelProvided_shouldReturnThatRespectingSizeLimit() = runTest {
//        MemorizationLevel.values().forEach { memorizationLevel ->
//            val numberOfFlashCards = 7
//            val flashCards = List(numberOfFlashCards) {
//                FlashCardFactory.createNewFlashCard()
//                    .copy(memorizationLevel = memorizationLevel)
//            }
//
//            // when size limit lesser than the flash cards size
//            val sizeLimit1 = numberOfFlashCards - 1
//            val selector1 = PracticeFlashCardSelector(
//                flashCards = flashCards,
//                sizeLimit = sizeLimit1,
//                memorizationLevelPercentages = mapOf(
//                    memorizationLevel to 100
//                )
//            )
//            assertEquals(flashCards.subList(0, sizeLimit1), selector1.select())
//
//            // when size limit greater than the flash cards size
//            val sizeLimit2 = numberOfFlashCards + 1
//            val selector2 = PracticeFlashCardSelector(
//                flashCards = flashCards,
//                sizeLimit = sizeLimit2,
//                memorizationLevelPercentages = mapOf(
//                    memorizationLevel to 100
//                )
//            )
//            assertEquals(flashCards, selector2.select())
//
//            flashCards
//        }
//    }
//
//    @Test
//    fun whenTwoMemorizationLevelRequested_shouldReturnFlashCardsAccordingToPercentages_andShouldRespectTheSizeLimit() {
//        MemorizationLevel.values().forEach { memorizationLevel ->
//            val twoMemorizationLevels = MemorizationLevel.values()
//                .toMutableList().also {
//                    it.remove(memorizationLevel)
//                }
//
//            val flashCardsSize = 20
//            val flashCards = List(flashCardsSize) {
//                FlashCardFactory.createNewFlashCard()
//                    .copy(memorizationLevel = twoMemorizationLevels[it % 2])
//            }
//            val selector1 = PracticeFlashCardSelector(
//                flashCards = emptyList(),
//                sizeLimit = 10,
//                memorizationLevelPercentages = mapOf(
//                    twoMemorizationLevels[0] to 50,
//                    twoMemorizationLevels[1] to 50,
//                )
//            )
//
//        }
//    }
}