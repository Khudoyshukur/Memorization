package uz.androdev.memorization.data.fake

import kotlinx.coroutines.flow.*
import uz.androdev.memorization.data.db.dao.FlashCardDao
import uz.androdev.memorization.model.entity.FlashCardEntity
import uz.androdev.memorization.model.enums.MemorizationLevel

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

    override suspend fun getFlashCardById(id: Long): FlashCardEntity? {
        return flashCards.value.find { it.id == id }
    }

    override suspend fun removeFlashCard(id: Long) {
        flashCards.update { list ->
            list.filter { it.id != id }
        }
    }

    override suspend fun getFlashCardsRepetitionCountAscending(
        folderId: Long,
        memorizationLevel: MemorizationLevel
    ): List<FlashCardEntity> {
        return flashCards.value.filter {
            it.folderId == folderId &&
                    it.memorizationLevel == memorizationLevel
        }.sortedBy { it.repetitionCount }
    }

    override suspend fun getFlashCardsRepetitionCountDescending(
        folderId: Long,
        memorizationLevel: MemorizationLevel
    ): List<FlashCardEntity> {
        return flashCards.value.filter {
            it.folderId == folderId &&
                    it.memorizationLevel == memorizationLevel
        }.sortedByDescending { it.repetitionCount }
    }
}