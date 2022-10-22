package uz.androdev.memorization.factory

import com.github.javafaker.Faker
import uz.androdev.memorization.model.input.FolderInput
import uz.androdev.memorization.model.model.Folder
import java.util.*

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 10:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FolderFactory {
    fun createUniqueFolderInput(): FolderInput {
        return FolderInput(
            title = UUID.randomUUID().toString()
        )
    }

    fun createFolder(): Folder {
        val faker = Faker()
        return Folder(
            id = randomId(),
            title = faker.funnyName().name()
        )
    }

    private fun randomId(): Long {
        return (Long.MIN_VALUE..Long.MAX_VALUE).random()
    }
}