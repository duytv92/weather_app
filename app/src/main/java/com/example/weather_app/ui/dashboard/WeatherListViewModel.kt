import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weather_app.model.weather.ListItem
import kotlinx.coroutines.launch

class WeatherListViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {
    var listWeather: MutableState<List<ListItem>> = mutableStateOf(emptyList())
    var isLoading: MutableState<Boolean> = mutableStateOf(false)
    var msg: MutableState<String> = mutableStateOf("")

    private val CITY_NAME = "q"
    private val NUMBER_FORECAST = "cnt"
    private val APP_ID = "appid"
    private val NOT_FOUND = "City not found"
    private val APP_ID_VALUE = "60c6fbeb4b93ac653c492ba806fc346d"
    private val NUMBER_FORECAST_VALUE = "7"

    fun fetchWeather(cityName: String = "hanoi") {
        listWeather.value = emptyList()
        msg.value = "";
        viewModelScope.launch {
            try {
                isLoading.value = true
                val filter = HashMap<String, String>()
                filter[CITY_NAME] = cityName
                filter[NUMBER_FORECAST] = NUMBER_FORECAST_VALUE
                filter[APP_ID] = APP_ID_VALUE
                //filter["units"] = "Temperature"
                val result = weatherRepository.getForeCast(filter)
                listWeather.value = result.listItem
                isLoading.value = false

                /*result.enqueue(object :
                    Callback<WeatherResponse> {
                    override fun onResponse(
                        call: Call<WeatherResponse>,
                        response: Response<WeatherResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val post = response.body()
                            if (post != null) {
                                listWeather.value = post.listItem
                            }
                            // Handle the retrieved post data
                        } else {
                            // Handle error
                            msg.value = NOT_FOUND;
                        }
                    }

                    override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                        // Handle failure
                    }
                })*/
            } catch (exception: Exception) {
                msg.value = NOT_FOUND
                isLoading.value = false
            }

        }
    }

}

class WeatherListViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherListViewModel::class.java)) {
            return WeatherListViewModel(WeatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}