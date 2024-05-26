import com.example.weather_app.utils.Utils
import com.google.gson.GsonBuilder
import net.vrallev.android.context.AppContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/"
    private var retrofit: Retrofit? = null

    //caching
    private val CACHE_SIZE: Long = (10 * 1024 * 1024).toLong()
    private const val CONNECT_TIMEOUT = 60L
    private const val CACHE_CONTROL = "Cache-Control"
    private const val TIME_CACHE_ONLINE = "public, max-age = 60" // 1 minute//"public, max-age=" + 5
    private const val TIME_CACHE_OFFLINE =
        "public, only-if-cached, max-stale = 86400" //1 day//"public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7

    val apiService: ApiService = getRetrofitInstance().create(ApiService::class.java)
    private fun getRetrofitInstance(): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: buildRetrofitApi().also { retrofit = it }
        }
    }

    private fun buildRetrofitApi(): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()

        val context = AppContext.get()

        val logging = HttpLoggingInterceptor()
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            //Caching
            .cache(
                Cache(
                    File(context.cacheDir, "http_cache"),
                    CACHE_SIZE
                )
            )
            .addInterceptor { chain ->
                var request = chain.request()
                request = if (Utils.isConnected())
                    request.newBuilder().header(CACHE_CONTROL, TIME_CACHE_ONLINE).build()
                else
                    request.newBuilder().header(
                        CACHE_CONTROL,
                        TIME_CACHE_OFFLINE
                    ).build()
                chain.proceed(request)
            }
            //End Cache
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            // we need to add converter factory to
            // convert JSON object to Java object
            .build()
    }
}