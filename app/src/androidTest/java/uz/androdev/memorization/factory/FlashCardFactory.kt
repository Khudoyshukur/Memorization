package uz.androdev.memorization.factory

import com.github.javafaker.Faker
import uz.androdev.memorization.model.model.FlashCard

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 12:20 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FlashCardFactory {
    private val faker = Faker()

    fun createNewFlashCard(): FlashCard {
        return FlashCard(
            id = 1,
            question = faker.lorem().characters(),
            answer = faker.lorem().characters()
        )
    }

    fun createFlashCards(size: Int): List<FlashCard> {
        return List(size) {
            FlashCard(
                id = it.toLong(),
                question = faker.lorem().characters(),
                answer = faker.lorem().characters()
            )
        }
    }
}