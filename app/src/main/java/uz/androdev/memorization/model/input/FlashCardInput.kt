package uz.androdev.memorization.model.input

import org.threeten.bp.LocalDateTime

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:41 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

data class FlashCardInput(
    val folderId: Long,
    val question: String,
    val answer: String,
    val dateTime: LocalDateTime = LocalDateTime.now()
)