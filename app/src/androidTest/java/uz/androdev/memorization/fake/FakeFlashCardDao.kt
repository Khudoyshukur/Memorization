package uz.androdev.memorization.fake

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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
    private val flashCards = MutableStateFlow<List<FlashCardEntity>>(emptyList())

    override suspend fun insertFlashCard(flashCardEntity: FlashCardEntity): Long {
        flashCards.update {
            it + listOf(flashCardEntity)
        }
        return flashCardEntity.id
    }

    override fun getAllFlashCards(folderId: Long): Flow<List<FlashCardEntity>> {
        return flashCards.map {
            it.filter { it.folderId == folderId }
        }
    }

}