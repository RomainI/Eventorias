package fr.ilardi.eventorias.di

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }



    @Provides
    @Singleton
//    @PlacesRetrofit
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GooglePlacesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    @Singleton
    fun provideGooglePlacesApi( retrofit: Retrofit): GooglePlacesApi {
        return retrofit.create(GooglePlacesApi::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthUI(): AuthUI {
        return AuthUI.getInstance()
    }

}

