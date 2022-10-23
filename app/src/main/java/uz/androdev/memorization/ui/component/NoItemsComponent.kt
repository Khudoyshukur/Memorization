package uz.androdev.memorization.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uz.androdev.memorization.R

/**
 * Created by: androdev
 * Date: 22-10-2022
 * Time: 12:45 PM
 * Email: Khudoyshukur.Juraev.001@mail.ru
 */

@Composable
fun NoItemsComponent(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(id = R.drawable.img_empty_folder),
            contentDescription = stringResource(id = R.string.empty_folder)
        )
        Text(text = message)
    }
}

@Preview
@Composable
fun Preview() {
    NoItemsComponent(message = "You have no items")
}