package uz.androdev.memorization.ui.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.androdev.memorization.ui.navigation.Arguments.ARGUMENT_FOLDER_ID
import uz.androdev.memorization.ui.screen.FlashCardScreenRoute
import uz.androdev.memorization.ui.screen.FolderScreenRoute
import uz.androdev.memorization.ui.screen.PracticeFlashCardScreenRoute

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 11:06 AM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

sealed class Screen(val route: String) {
    object FoldersScreen : Screen("folders_screen")
    object FlashCardsScreen : Screen("flash_cards/{${ARGUMENT_FOLDER_ID}}") {
        fun getNavigateRoute(folderId: Long): String {
            return "flash_cards/$folderId"
        }
    }
    object PracticeFlashCardsScreen : Screen("practice_flash_cards/{${ARGUMENT_FOLDER_ID}}") {
        fun getNavigateRoute(folderId: Long): String {
            return "practice_flash_cards/$folderId"
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.FoldersScreen.route) {
        composable(Screen.FoldersScreen.route) {
            FolderScreenRoute(
                onNavigateToItemsScreen = {
                    navController.navigate(
                        Screen.FlashCardsScreen.getNavigateRoute(it.id)
                    )
                },
                onNavigateToPracticeScreen = {
                    navController.navigate(
                        Screen.PracticeFlashCardsScreen.getNavigateRoute(it.id)
                    )
                }
            )
        }

        composable(
            Screen.FlashCardsScreen.route,
            arguments = listOf(
                navArgument(ARGUMENT_FOLDER_ID) { type = NavType.LongType }
            )
        ) {
            FlashCardScreenRoute()
        }

        composable(
            Screen.PracticeFlashCardsScreen.route,
            arguments = listOf(
                navArgument(ARGUMENT_FOLDER_ID) { type = NavType.LongType }
            )
        ) {
            PracticeFlashCardScreenRoute(
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}