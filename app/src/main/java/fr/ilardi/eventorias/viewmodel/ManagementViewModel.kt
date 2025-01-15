package fr.ilardi.eventorias.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.model.User
import fr.ilardi.eventorias.repository.AuthenticationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(private val authenticationRepository: AuthenticationRepository): ViewModel() {


    private val _user = MutableStateFlow<FirebaseUser?>(authenticationRepository.getCurrentUser())
    val user: StateFlow<FirebaseUser?> = _user


    fun uploadImageAndUpdateProfile(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val downloadUrl = authenticationRepository.uploadImageToStorage(imageUri)

                authenticationRepository.updateUserProfilePhoto(downloadUrl)

                _user.value = authenticationRepository.getCurrentUser()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun switchNotification(turnOn: Boolean) {
        viewModelScope.launch {
            try {
                authenticationRepository.activateNotification(turnOn)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}