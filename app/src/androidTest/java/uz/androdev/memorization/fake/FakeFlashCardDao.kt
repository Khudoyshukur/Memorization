package uz.androdev.memorization.fake

import kotlinx.coroutines.flow.*
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.model.entity.FlashCardEntity
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 29-10-2022
 * Time: 2:58 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class FakeFlashCardDao @Inject constructor() : FlashCardDao {
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

    override suspend fun getFlashCardById(id: Long): FlashCardEntity? {
        return flashCards.value.find {
            it.id == id
        }
    }

    override suspend fun removeFlashCard(id: Long) {
        flashCards.update { list ->
            list.filter { it.id != id }
        }
    }
}