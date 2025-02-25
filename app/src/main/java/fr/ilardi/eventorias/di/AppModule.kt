package fr.ilardi.eventorias.di

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.ilardi.eventorias.repository.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * AppModule provides dependency injection for APIs (Firebase, Retrofit, Google Maps)
 * and repositories used in the application, for Hilt usage
 */

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideAuthUI(): AuthUI = AuthUI.getInstance()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GooglePlacesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
    }

    @Provides
    @Singleton
    fun provideGooglePlacesApi(retrofit: Retrofit): GooglePlacesApi {
        return retrofit.create(GooglePlacesApi::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthenticationRepository(
        firebaseAuth: FirebaseAuth,
        storage: FirebaseStorage,
        firestore: FirebaseFirestore,
        authUI: AuthUI
    ): IAuthenticationRepository {
        return AuthenticationRepository(firebaseAuth, storage, firestore, authUI)
    }

    @Provides
    @Singleton
    fun provideEventRepository(
        firestore: FirebaseFirestore
    ): IEventRepository {
        return FirestoreEventRepository(firestore)
    }
}