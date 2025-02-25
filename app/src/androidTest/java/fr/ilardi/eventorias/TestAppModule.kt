package fr.ilardi.eventorias

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.ilardi.eventorias.di.AppModule
import fr.ilardi.eventorias.di.GooglePlacesApi
import fr.ilardi.eventorias.repository.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Used to replace AppModule for UI testing for Hilt
 * Using fake repositories and fake APIs used in tests
 */

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFakeFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFakeFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFakeFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFakeAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    @Singleton
    fun provideFakeGooglePlacesApi(): GooglePlacesApi {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fake-api.com/") // Fake API URL pour éviter les erreurs réseau
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
        return retrofit.create(GooglePlacesApi::class.java)
    }


    @Provides
    @Singleton
    fun provideFakeGooglePlacesRepository(): IGooglePlacesRepository {
        return FakeGooglePlacesRepository()
    }

    @Provides
    @Singleton
    fun provideFakeAuthenticationRepository(): IAuthenticationRepository {
        return FakeAuthenticationRepository()
    }

    @Provides
    @Singleton
    fun provideFakeFirestoreEventRepository(): IEventRepository {
        return FakeFirestoreEventRepository()
    }
}