package uz.androdev.memorization.factory

import com.github.javafaker.Faker
import org.threeten.bp.LocalDateTime
import uz.androdev.memorization.model.entity.FolderEntity

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 10:23 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

object FolderEntityFactory {
    fun createFolderEntityWithoutId(): FolderEntity {
        val faker = Faker()
        return FolderEntity(
            title = faker.funnyName().name(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    fun createFolderEntityWithoutIdAndWith(title: String): FolderEntity {
        return FolderEntity(
            title = title,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }
}