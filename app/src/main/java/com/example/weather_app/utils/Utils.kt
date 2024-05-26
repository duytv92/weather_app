package com.example.weather_app.utils

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import net.vrallev.android.context.AppContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {
    private const val RFC_1123_DATE_TIME = "EEE, dd MMM yyyy";
    fun getStrDate(timeLong: Long): String {
        val simpleDateFormat: SimpleDateFormat = SimpleDateFormat(RFC_1123_DATE_TIME, Locale.US)
        val date = Date(timeLong * 1000)
        return simpleDateFormat.format(date)
    }

    fun convertKelvinToCelsius(tempKelvin: Long): String {
        val tempCelsius = tempKelvin - 273.15
        return "${tempCelsius.toInt()} ℃"
    }

    fun isConnected(): Boolean {
        val context = AppContext.get()
        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        // Network class represents one of the networks that the device is connected to.
        val activeNetwork = connectivityManager.activeNetwork
        return if (activeNetwork == null) {
            false // if there is no active network, then simply no internet connection.
        } else {
            // NetworkCapabilities object contains information about properties of a network
            val netCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            (netCapabilities != null
                    // indicates that the network is set up to access the internet
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    // indicates that the network provides actual access to the public internet when it is probed
                    && netCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED))
        }
    }
}