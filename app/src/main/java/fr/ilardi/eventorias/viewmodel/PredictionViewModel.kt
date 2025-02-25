package fr.ilardi.eventorias.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.di.GooglePrediction
import fr.ilardi.eventorias.repository.GooglePlacesRepository
import fr.ilardi.eventorias.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * PredictionViewModel is used to predict address
 * from Google Places API based on user String input.
 */

@HiltViewModel
class PredictionViewModel @Inject constructor(private val googleRepository: GooglePlacesRepository): ViewModel(){

    val isLoading = mutableStateOf(false)
    val predictions = mutableStateOf(ArrayList<GooglePrediction>())
//    init{
//        getPredictions("7 rue de la chapelle")
//        for(address in predictions.value){
//            ("TEST ADDRESS", address.description)
//        }
//    }

    fun updatePredictions(address: String) {
        viewModelScope.launch {
            isLoading.value = true
            val response = googleRepository.getPredictions(input = address)
            when(response){
                is Resource.Success -> {
                    predictions.value = response.data?.predictions!!
                }

                is Resource.Error -> null
                is Resource.Loading -> null
            }

            isLoading.value = false
        }
    }

    fun onSearchAddressChange(address: String){
        updatePredictions(address)
    }
}