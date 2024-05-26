import com.example.weather_app.model.weather.WeatherResponse

object WeatherRepository {
    private val apiHelper = RetrofitHelper.apiService

    suspend fun getForeCast(filter: HashMap<String, String>): WeatherResponse {
        return apiHelper.getForeCast(filter)
    }

}