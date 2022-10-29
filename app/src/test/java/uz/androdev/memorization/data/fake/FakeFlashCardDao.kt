package uz.androdev.memorization.data.fake

import kotlinx.coroutines.flow.*
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.model.entity.FlashCardEntity

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 12:16 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FakeFlashCardDao : FlashCardDao {
    private var currentId = 1L
    private val flashCards = MutableStateFlow<List<FlashCardEntity>>(emptyList())

    override suspend fun insertFlashCard(flashCardEntity: FlashCardEntity): Long {
        val folder = flashCards.value.find {
            it.id == flashCardEntity.id
        }

        return if (folder == null) {
            flashCards.update {
                it + listOf(flashCardEntity.copy(id = currentId))
            }

            currentId++
        } else {
            flashCards.update {
                it.map {
                    if (it.id == flashCardEntity.id) {
                        flashCardEntity
                    } else {
                        it
                    }
                }
            }

            flashCardEntity.id
        }
    }

    override fun getAllFlashCards(folderId: Long): Flow<List<FlashCardEntity>> {
        return flashCards.map {
            it.filter { it.folderId == folderId }
        }
    }

}