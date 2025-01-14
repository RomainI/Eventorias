package fr.ilardi.eventorias.repository

import android.util.Log
import fr.ilardi.eventorias.di.GooglePlacesApi
import fr.ilardi.eventorias.di.GooglePredictionsResponse
import fr.ilardi.eventorias.utils.Resource
import javax.inject.Inject

class GooglePlacesRepository @Inject constructor(
     private val api: GooglePlacesApi,
){
    suspend fun getPredictions(input: String): Resource<GooglePredictionsResponse> {

//        Log.d("GooglePlacesRepository", "input "+input)
//        Log.d("GooglePlacesRepository","try "+api.getPredictions(input = input).predictions.get(0).description)
        val response = try {
            api.getPredictions(input = input)
        } catch (e: Exception) {
//            Log.d("Rently", "Exception: ${e}")
            return Resource.Error("Failed prediction")
        }
        return Resource.Success(response)
    }
}