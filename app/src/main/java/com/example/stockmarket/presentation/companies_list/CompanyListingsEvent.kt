package com.example.stockmarket.presentation.companies_list

sealed class CompanyListingsEvent {
    object Refresh : CompanyListingsEvent()
    data class OnSearchQueryChange(val query: String) : CompanyListingsEvent()
}
