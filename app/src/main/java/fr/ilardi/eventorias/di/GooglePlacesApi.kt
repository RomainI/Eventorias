package fr.ilardi.eventorias.di

import fr.ilardi.eventorias.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for Google Places API.
 * Answer will be mapped in a GooglePredictionsResponse object
 */

interface GooglePlacesApi {
    @GET("maps/api/place/autocomplete/json")
    suspend fun getPredictions(
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
        @Query("types") types: String = "address",
        @Query("input") input: String
    ): GooglePredictionsResponse

    companion object {
        const val BASE_URL = "https://maps.googleapis.com/"
    }
}