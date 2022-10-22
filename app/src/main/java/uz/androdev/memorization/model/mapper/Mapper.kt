package uz.androdev.memorization.model.mapper

/**
 * Created by: androdev
 * Date: 21-10-2022
 * Time: 5:54 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

interface Mapper<in Source : Any, out Target : Any> {
    fun map(source: Source): Target
}