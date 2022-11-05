package uz.androdev.memorization.model.input

import uz.androdev.memorization.model.enums.MemorizationLevel

/**
 * Created by: androdev
 * Date: 01-11-2022
 * Time: 3:18 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

typealias Percentage = Int

data class PracticeFlashCardsFilterInput(
    val folderId: Long,
    val size: Int,
    val memorizationLevelPercentages: Map<MemorizationLevel, Percentage>
)

