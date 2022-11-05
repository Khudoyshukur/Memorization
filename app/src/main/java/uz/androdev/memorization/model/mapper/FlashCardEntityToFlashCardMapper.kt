package uz.androdev.memorization.model.mapper

import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.model.FlashCard
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 05-11-2022
 * Time: 11:13 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FlashCardEntityToFlashCardMapper @Inject constructor() {
    operator fun invoke(entity: FlashCardEntity): FlashCard {
        return FlashCard(
            id = entity.id,
            question = entity.question,
            answer = entity.answer,
            memorizationLevel = entity.memorizationLevel
        )
    }
}