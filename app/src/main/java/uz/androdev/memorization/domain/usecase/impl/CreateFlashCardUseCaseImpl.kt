package uz.androdev.memorization.domain.usecase.impl

import android.database.sqlite.SQLiteConstraintException
import uz.androdev.memorization.data.repository.FlashCardRepository
import uz.androdev.memorization.domain.response.UseCaseResponse
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCase
import uz.androdev.memorization.domain.usecase.CreateFlashCardUseCaseFailure
import uz.androdev.memorization.model.input.FlashCardInput
import javax.inject.Inject

/**
 * Created by: androdev
 * Date: 24-10-2022
 * Time: 3:15 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CreateFlashCardUseCaseImpl @Inject constructor(
    private val flashCardRepository: FlashCardRepository
) : CreateFlashCardUseCase {
    override suspend fun invoke(flashCardInput: FlashCardInput): UseCaseResponse<Unit, CreateFlashCardUseCaseFailure> {
        return try {
            flashCardRepository.createFlashCard(flashCardInput)
            UseCaseResponse.Success(Unit)
        } catch (e: SQLiteConstraintException) {
            UseCaseResponse.Failure(CreateFlashCardUseCaseFailure.FolderDoesNotExist)
        } catch (t: Throwable) {
            UseCaseResponse.Failure(CreateFlashCardUseCaseFailure.CouldNotCreateFlashCard)
        }
    }
}

