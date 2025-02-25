package fr.ilardi.eventorias.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.repository.AuthenticationRepository
import javax.inject.Inject

/**
 * LoginViewModel is used to provide an intent for user sign-in
 */

@HiltViewModel
class LoginViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository): ViewModel() {

    fun getLoginIntent() : Intent {
        return authenticationRepository.getSignInIntent()
    }
}
