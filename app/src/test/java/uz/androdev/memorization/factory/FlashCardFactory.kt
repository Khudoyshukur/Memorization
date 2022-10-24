package uz.androdev.memorization.factory

import com.github.javafaker.Faker
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.input.FlashCardInput

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 12:24 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FlashCardFactory {
    fun createFlashCardInput(): FlashCardInput {
        val faker = Faker()
        return FlashCardInput(
            folderId = 1,
            question = faker.lorem().characters(),
            answer = faker.lorem().characters(),
            dateTime = LocalDateTime.now()
        )
    }

    fun createNewFlashCard(id: Long = 0, folderId: Long): FlashCardEntity {
        val faker = Faker()
        return FlashCardEntity(
            id = id,
            folderId = folderId,
            question = faker.lorem().characters(),
            answer = faker.lorem().characters()
        )
    }
}