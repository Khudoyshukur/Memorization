package uz.androdev.memorization.hilt

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Created by: androdev
 * Date: 23-10-2022
 * Time: 12:57 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}