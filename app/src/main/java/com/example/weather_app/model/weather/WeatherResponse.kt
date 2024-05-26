package com.example.weather_app.model.weather

import com.google.gson.annotations.SerializedName


data class WeatherResponse(

    @SerializedName("city") var city: City? = City(),
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Double? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var listItem: ArrayList<ListItem> = arrayListOf(),

    )