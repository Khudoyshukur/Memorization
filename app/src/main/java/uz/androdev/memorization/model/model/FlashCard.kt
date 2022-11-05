package uz.androdev.memorization.model.model

import uz.androdev.memorization.model.enums.MemorizationLevel

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:50 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class FlashCard(
    val id: Long,
    val question: String,
    val answer: String,
    val memorizationLevel: MemorizationLevel
)