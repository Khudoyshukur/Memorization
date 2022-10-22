package uz.androdev.memorization.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import uz.androdev.memorization.ui.component.FoldersListComponent
import uz.androdev.memorization.ui.theme.MemorizationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemorizationTheme {
                // A surface container using the 'background' color from the theme
                FoldersListComponent(
                    folders = emptyList()
//                            List(100) {
//                        Folder(
//                            id = it.toLong(),
//                            title = UUID.randomUUID().toString().substring(0,10)
//                        )
//                    }
                )
            }
        }
    }
}