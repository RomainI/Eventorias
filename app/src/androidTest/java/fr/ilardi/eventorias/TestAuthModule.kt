package fr.ilardi.eventorias

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import fr.ilardi.eventorias.di.AppModule
import fr.ilardi.eventorias.repository.AuthenticationRepository
import fr.ilardi.eventorias.repository.GooglePlacesRepository
import io.mockk.every
import io.mockk.mockk
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class] // Remplace AppModule uniquement pour les tests
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideMockFirebaseAuth(): FirebaseAuth {
        val mockAuth = mockk<FirebaseAuth>(relaxed = true)

        every { mockAuth.currentUser } returns null // Simule un utilisateur déconnecté

        return mockAuth
    }

    @Provides
    @Singleton
    fun provideMockAuthUI(): AuthUI {
        val mockAuthUI = mockk<AuthUI>(relaxed = true)

        every { mockAuthUI.createSignInIntentBuilder() } returns mockk(relaxed = true)

        return mockAuthUI
    }
}