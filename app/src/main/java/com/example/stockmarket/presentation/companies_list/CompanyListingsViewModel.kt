package com.example.stockmarket.presentation.companies_list

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.domain.repository.StockRepository
import com.example.stockmarket.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CompanyListingsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private var searchJob: Job? = null

    private val _state = mutableStateOf(CompanyListingsState())
    val state: State<CompanyListingsState> = _state

    fun onEvent(event: CompanyListingsEvent) {
        when (event) {
            CompanyListingsEvent.Refresh -> {
                getCompanyListings(fetchFromRemote = true)
            }
            is CompanyListingsEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListings()
                }
            }
        }
    }

    private fun getCompanyListings(
        query: String = _state.value.searchQuery.lowercase(Locale.ROOT),
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch {
            repository
                .getCompanyListings(fetchFromRemote, query)
                .collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { data ->
                                _state.value = _state.value.copy(
                                    companies = data,
                                    isLoading = false
                                )
                            }
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(isLoading = false)
                            // TODO: handle the error
                        }
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }
                    }
                }
        }
    }
}