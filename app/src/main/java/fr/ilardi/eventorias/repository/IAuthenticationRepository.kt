package fr.ilardi.eventorias.repository

import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import fr.ilardi.eventorias.model.User

/**
 * IAuthenticationRepository defines an interface for user authentication and profile management.
 * It takes place to allow testing by using a fake AppModule
 */

interface IAuthenticationRepository {
    fun isUserAuthenticated(): Boolean
    fun getCurrentUser(): User?
    fun getSignInIntent(): Intent
    suspend fun getUserByUid(uid: String): User?
    fun saveNewUserInFirebase()
    suspend fun uploadImageToStorage(imageUri: Uri): String
    suspend fun updateUserProfilePhoto(photoUrl: String)
    fun activateNotification(turnOn: Boolean)
}