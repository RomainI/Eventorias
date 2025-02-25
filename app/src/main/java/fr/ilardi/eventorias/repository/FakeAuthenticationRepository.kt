package fr.ilardi.eventorias.repository

import android.content.Intent
import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.MultiFactor
import fr.ilardi.eventorias.model.User

/**
 * FakeAuthenticationRepository is a AuthenticationRepository mock for testing purpose
 */


class FakeAuthenticationRepository : IAuthenticationRepository {

    private var fakeCurrentUser: User? = null
    private val usersDatabase = mutableMapOf<String, User>()

    override fun isUserAuthenticated(): Boolean {
        return fakeCurrentUser != null
    }

    override fun getCurrentUser(): User? {
        return fakeCurrentUser
    }

    override fun getSignInIntent(): Intent {
        return Intent().apply {
            putExtra("fakeSignIn", true)
        }
    }

    override suspend fun getUserByUid(uid: String): User? {
        return usersDatabase[uid]
    }

    override fun saveNewUserInFirebase() {
        fakeCurrentUser?.let { user ->
            val userData = User(
                name = user.name ?: "Fake User",
                email = user.email ?: "fakeuser@test.com",
                uid = user.uid,
                profileImage = user.profileImage?.toString() ?: ""
            )
            usersDatabase[user.uid] = userData
        }
    }

    override suspend fun uploadImageToStorage(imageUri: Uri): String {
        return "https://fake-storage.com/${System.currentTimeMillis()}.jpg"
    }

    override suspend fun updateUserProfilePhoto(photoUrl: String) {
        fakeCurrentUser = fakeCurrentUser?.copy(profileImage = Uri.parse(photoUrl).toString())
        saveNewUserInFirebase()
    }

    override fun activateNotification(turnOn: Boolean) {
    }

}

