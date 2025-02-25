package fr.ilardi.eventorias.repository

import fr.ilardi.eventorias.di.GooglePlacesApi
import fr.ilardi.eventorias.di.GooglePredictionsResponse
import fr.ilardi.eventorias.utils.Resource
import javax.inject.Inject


/**
 * GooglePlacesRepository is used to push place predictions from the Google Places API.
 */

class GooglePlacesRepository @Inject constructor(
    private val api: GooglePlacesApi
) : IGooglePlacesRepository {

    override suspend fun getPredictions(input: String): Resource<GooglePredictionsResponse> {
        return try {
            val response = api.getPredictions(input = input)
            Resource.Success(response)
        } catch (e: Exception) {
            Resource.Error("Failed to get predictions: ${e.message}")
        }
    }
}