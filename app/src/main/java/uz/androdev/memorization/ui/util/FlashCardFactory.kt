package uz.androdev.memorization.ui.util

import uz.androdev.memorization.model.enums.MemorizationLevel
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 17-11-2022
 * Time: 2:57 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FlashCardFactory {
    fun createFlashCard(): FlashCard {
        return FlashCard(
            id = 1L,
            question = "What is this?",
            answer = "This is a FlashCard",
            memorizationLevel = MemorizationLevel.LOW
        )
    }
}