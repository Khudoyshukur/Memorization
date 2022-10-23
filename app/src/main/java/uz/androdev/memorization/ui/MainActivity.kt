package uz.androdev.memorization.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.androdev.memorization.ui.navigation.AppNavHost
import uz.androdev.memorization.ui.theme.MemorizationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemorizationTheme {
                // A surface container using the 'background' color from the theme
                AppNavHost()
            }
        }
    }
}