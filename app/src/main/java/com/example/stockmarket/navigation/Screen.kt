package com.example.stockmarket.navigation

sealed class Screen(val route: String) {
    object CompanyListingsScreen : Screen("company_listings_screen")
}
