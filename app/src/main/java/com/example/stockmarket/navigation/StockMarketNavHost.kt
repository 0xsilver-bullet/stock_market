package com.example.stockmarket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.stockmarket.presentation.companies_list.CompanyListingsScreen

@Composable
fun StockMarketNavHost(navHostController: NavHostController) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.CompanyListingsScreen.route
    ) {
        composable(Screen.CompanyListingsScreen.route) {
            CompanyListingsScreen(navHostController)
        }
    }
}