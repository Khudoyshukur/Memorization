package uz.androdev.memorization.factory

import com.github.javafaker.Faker
import uz.androdev.memorization.model.entity.FlashCardEntity

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 11:07 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FlashCardEntityFactory {

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