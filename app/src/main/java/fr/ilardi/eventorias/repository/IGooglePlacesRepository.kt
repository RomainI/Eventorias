package fr.ilardi.eventorias.repository

import fr.ilardi.eventorias.di.GooglePredictionsResponse
import fr.ilardi.eventorias.utils.Resource

/**
 * IGooglePlacesRepository defines an interface for Google Places inputs.
 * It takes place to allow testing by using a fake AppModule
 */

interface IGooglePlacesRepository {
    suspend fun getPredictions(input: String): Resource<GooglePredictionsResponse>
}