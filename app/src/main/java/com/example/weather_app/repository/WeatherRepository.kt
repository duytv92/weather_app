import com.example.weather_app.model.weather.WeatherResponse
import retrofit2.Call

object WeatherRepository {
    private val apiHelper = RetrofitHelper.apiService

    fun getForeCast(filter: HashMap<String, String>): Call<WeatherResponse> {
        return apiHelper.getForeCast(filter)
    }

}