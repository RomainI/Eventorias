package fr.ilardi.eventorias.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.squareup.okhttp.ResponseBody
import fr.ilardi.eventorias.di.GoogleStaticMapsApi
import java.io.InputStream
import javax.inject.Inject

class GoogleStaticMapsRepository @Inject constructor(
    private val api: GoogleStaticMapsApi
){
suspend fun fetchStaticMap(center: String): Bitmap? {
    return try {
        val response = api.getStaticMap(center = center)
        convertResponseToBitmap(response)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


private fun convertResponseToBitmap(responseBody: ResponseBody): Bitmap? {
        return try {
            val inputStream: InputStream = responseBody.byteStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}