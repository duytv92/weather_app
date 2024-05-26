package com.example.weather_app

import DashBoardRoute
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weather_app.Destinations.DASHBOARD_ROUTE

object Destinations {
    const val DASHBOARD_ROUTE = "dashboard/"
}
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = DASHBOARD_ROUTE) {

        composable(DASHBOARD_ROUTE){
            DashBoardRoute()
        }
    }
}
