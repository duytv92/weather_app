import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weather_app.model.weather.ListItem
import com.example.weather_app.ui.theme.TopBar
import com.example.weather_app.ui.theme.bgBtnGetWeather
import com.example.weather_app.ui.theme.bgList
import com.example.weather_app.ui.theme.indicatorColor
import com.example.weather_app.ui.theme.txtColorGetWeather
import com.example.weather_app.ui.theme.txtItem
import com.example.weather_app.utils.Utils

@Composable
fun DashBoardScreen() {
    val weatherListViewModel: WeatherListViewModel =
        viewModel(factory = WeatherListViewModelFactory())

    WeatherList(weather = weatherListViewModel.listWeather.value, weatherListViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherList(weather: List<ListItem>, weatherListViewModel: WeatherListViewModel) {

    LaunchedEffect(Unit, block = {
        weatherListViewModel.fetchWeather()
    })


    val txtState = remember {
        mutableStateOf("hanoi")
    }
    Column {
        TopBar()
        TextFieldView(txtState)
        //SearchBar(weatherListViewModel)
        BtnGetWeather(weatherListViewModel, txtState.value)
        if (weatherListViewModel.isLoading.value) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = indicatorColor,
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .align(Alignment.Center)
                )
            }
        } else if (weather.isEmpty() && weatherListViewModel.msg.value.isNotEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = weatherListViewModel.msg.value,
                    color = indicatorColor
                )
            }
        } else {
            LazyColumn {
                itemsIndexed(items = weather) { index, item ->
                    Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                        Column(
                            modifier = Modifier.background(color = bgList)
                        ) {
                            Text(
                                text = "Date: ${Utils.getStrDate(item.dt!!.toLong())}",
                                color = txtItem
                            )
                            Text(
                                text = "Average temperature: ${Utils.convertKelvinToCelsius(item.temp!!.day!!.toLong())}",
                                color = txtItem
                            )
                            Text(text = "Pressure: ${item.pressure}", color = txtItem)
                            Text(text = "Humidity: ${item.humidity}%", color = txtItem)
                            Text(
                                text = "Description: ${item.weather[0].description}",
                                color = txtItem
                            )
                            if (index < weather.size - 1) Divider(
                                color = Color.Gray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }

}

@ExperimentalMaterial3Api
@Composable
fun TopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TopBar, titleContentColor = Color.White
        ), title = ({ Text(text = "Weather Forecast") })
    )
}

@ExperimentalMaterial3Api
@Composable
fun TextFieldView(txtState: MutableState<String>) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 0.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = indicatorColor,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedLabelColor = indicatorColor,
            focusedTextColor = txtColorGetWeather,
            cursorColor = indicatorColor,
        ),
        value = txtState.value,
        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
        onValueChange = {
            txtState.value = it
        },
    )
}

@ExperimentalMaterial3Api
@Composable
fun BtnGetWeather(viewModel: WeatherListViewModel, searchStr: String) {
    val context = LocalContext.current
    val controller = LocalSoftwareKeyboardController.current
    Button(
        shape = RoundedCornerShape(topStart = 5.dp, bottomEnd = 5.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgBtnGetWeather
        ),
        onClick = {
            controller?.hide()
            if (searchStr.length >= 3) {
                viewModel.fetchWeather(searchStr)
            } else {
                Toast.makeText(
                    context,
                    "You need to enter 3 or more characters",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }) {
        Text(
            text = "Get Weather",
            color = txtColorGetWeather,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun SearchBar(viewModel: WeatherListViewModel) {
    var txt by remember { mutableStateOf("hanoi") }
    var active by remember { mutableStateOf(false) }
    val items = remember {
        mutableListOf("hanoi", "saigon")
    }
    SearchBar(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 5.dp),
        query = txt,
        onQueryChange = { txt = it },
        onSearch = {
            if (!items.contains(it)) {
                items.add(it)
            }
            active = false
            viewModel.fetchWeather(it)
        },
        active = active,
        onActiveChange = { active = it },
        placeholder = { Text(text = "Search") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon"
            )
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = Modifier.clickable {
                        if (txt.isNotEmpty()) {
                            txt = ""
                        } else {
                            active = false
                        }

                    }, imageVector = Icons.Default.Close, contentDescription = "Close Icon"
                )
            }
        }) {
        items.forEach {
            Row(modifier = Modifier
                .padding(all = 14.dp)
                .clickable {
                    txt = it
                }) {
                Icon(
                    modifier = Modifier.padding(end = 10.dp),
                    imageVector = Icons.Default.History,
                    contentDescription = "History"
                )
                Text(text = it)
            }
        }
    }
}

@Preview
@Composable
fun PreViewWelcome() {
    DashBoardScreen()
}