package uz.androdev.memorization.data.util

import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.input.Percentage
import uz.androdev.memorization.model.input.PracticeFlashCardsFilterInput
import uz.androdev.memorization.model.mapper.FlashCardEntityToFlashCardMapper
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Created by: androdev
 * Date: 01-11-2022
 * Time: 4:46 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class PracticeFlashCardSelector @Inject constructor(
    private val flashCardDao: FlashCardDao,
    private val flashCardEntityToFlashCardMapper: FlashCardEntityToFlashCardMapper
) {
    @Throws(IllegalArgumentException::class)
    suspend fun select(
        flashCardsFilterInput: PracticeFlashCardsFilterInput
    ): List<FlashCard> = with(flashCardsFilterInput) {
        if (size <= 0) {
            throw IllegalArgumentException("Size must be greater than 0.")
        }

        if (memorizationLevelPercentages.values.sum() != 100) {
            throw IllegalArgumentException("Memorization level percentages must sum up to 100.")
        }

        if (memorizationLevelPercentages.values.any { it <= 0 || it > 100 }) {
            throw IllegalArgumentException("Memorization level percentage is not within range [1:100]")
        }

        val result = arrayListOf<FlashCard>()
        for ((level, percentage) in memorizationLevelPercentages) {
            val flashCards = flashCardDao.getFlashCardsRepetitionCountDescending(
                folderId = folderId,
                memorizationLevel = level
            ).map(flashCardEntityToFlashCardMapper::invoke)

            val inputSize = (size * percentage / 100f).roundToInt()
            if (inputSize > result.size) {
                val delta = inputSize - result.size
                result.addAll(flashCards.subList(0, delta))
            } else {
                result.addAll(flashCards.subList(0, inputSize))

                if (inputSize == result.size) {
                    break
                }
            }
        }

        if (result.size < size) {
            val remaining = size - result.size
            for ((level, percentage) in memorizationLevelPercentages) {
                val flashCards = flashCardDao.getFlashCardsRepetitionCountDescending(
                    folderId = folderId,
                    memorizationLevel = level
                ).map(flashCardEntityToFlashCardMapper::invoke).filter {
                    !result.contains(it)
                }

                if (flashCards.size > remaining) {
                    result.addAll(flashCards.subList(0, remaining))
                    break
                } else {
                    result.addAll(flashCards)
                }
            }
        }

        return result
    }
}