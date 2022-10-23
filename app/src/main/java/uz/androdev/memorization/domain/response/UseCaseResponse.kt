package uz.androdev.memorization.domain.response

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 10:28 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed interface UseCaseResponse<out Data : Any, out Failure : UseCaseFailure> {
    data class Success<Data : Any>(val data: Data) : UseCaseResponse<Data, Nothing>
    data class Failure<Failure : UseCaseFailure>(val data: Failure) :
        UseCaseResponse<Nothing, Failure>
}

inline fun <Data : Any, Failure : UseCaseFailure> UseCaseResponse<Data, Failure>.onSuccess(
    action: (data: Data) -> Unit
) {
    if (this is UseCaseResponse.Success) {
        action.invoke(this.data)
    }
}

inline fun <Data : Any, Failure : UseCaseFailure> UseCaseResponse<Data, Failure>.onFailure(
    action: (failure: Failure) -> Unit
) {
    if (this is UseCaseResponse.Failure) {
        action.invoke(this.data)
    }
}