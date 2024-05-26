import com.example.weather_app.model.weather.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface ApiService {
    @GET("/data/2.5/forecast/daily")
    fun getForeCast(@QueryMap filter: HashMap<String, String>): Call<WeatherResponse>
}