package fr.ilardi.eventorias.di

import com.squareup.okhttp.ResponseBody
import fr.ilardi.eventorias.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleStaticMapsApi {

    @GET("maps/api/staticmap")
    suspend fun getStaticMap(
        @Query("key") key: String = BuildConfig.MAPS_API_KEY,
        @Query("center") center: String,
        @Query("zoom") zoom: String = "15",
        @Query("size") size: String="400x150",

    ): ResponseBody

//    companion object {
//        const val BASE_URL = "https://maps.googleapis.com/"
//    }
}

class GoogleStaticResponse {

}
