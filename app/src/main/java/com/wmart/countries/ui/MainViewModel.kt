package com.wmart.countries.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wmart.countries.model.Country
import com.wmart.countries.service.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiService: ApiService) : ViewModel() {
    var countryListResponse: List<Country> by mutableStateOf(listOf())

    val countryState = MutableStateFlow<CountryUIState>(CountryUIState.START)
    fun getCountryList() {
        viewModelScope.launch {
            apiService.getCountries().onSuccess {
                countryListResponse = it
                countryState.emit(CountryUIState.SUCCESS)
            }
                .onFailure {
                    countryState.emit(CountryUIState.FAILURE(it.localizedMessage))
                }


        }
    }
}

sealed class CountryUIState {
    object START : CountryUIState()
    object LOADING : CountryUIState()
    object SUCCESS : CountryUIState()
    data class FAILURE(val message: String) : CountryUIState()
}