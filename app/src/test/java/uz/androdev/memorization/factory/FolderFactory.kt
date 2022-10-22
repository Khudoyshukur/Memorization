package uz.androdev.memorization.factory

import uz.androdev.memorization.model.input.FolderInput
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
}