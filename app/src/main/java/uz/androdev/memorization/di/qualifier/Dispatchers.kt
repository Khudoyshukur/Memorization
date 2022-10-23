package uz.androdev.memorization.di.qualifier

import javax.inject.Qualifier

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 5:09 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
