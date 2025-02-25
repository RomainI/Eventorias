package fr.ilardi.eventorias.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.type.Date

import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.model.User
import kotlinx.coroutines.tasks.await
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AuthenticationRepository manages authentication.
 * It uses Firebase Authentication to provide user data
 */

@Singleton
class AuthenticationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val firestore: FirebaseFirestore,
    private val authUI: AuthUI
) : IAuthenticationRepository {

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser
        return firebaseUser?.let {
            User(
                name = it.displayName ?: "Unknown",
                email = it.email ?: "unknown@example.com",
                profileImage = it.photoUrl?.toString() ?: "",
                uid = it.uid
            )
        }
    }

    override fun getSignInIntent(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        return authUI.createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false, true)
            .setTheme(R.style.FirebaseLoginTheme)
            .setLogo(R.drawable.logo)
            .build()
    }

    override suspend fun getUserByUid(uid: String): User? {
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            if (document.exists()) {
                val name = document.getString("name") ?: ""
                val email = document.getString("email") ?: ""
                val photoUrl = document.getString("photoUrl") ?: ""
                User(name = name, email = email, uid = uid, profileImage = photoUrl)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Firestore", "Erreur : ${e.message}")
            null
        }
    }

    override fun saveNewUserInFirebase() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    val firestorePhotoUrl = document.getString("photoUrl")
                    if (!document.exists() || firestorePhotoUrl != currentUser.photoUrl.toString()) {
                        val userMap = mapOf(
                            "name" to currentUser.displayName,
                            "email" to currentUser.email,
                            "photoUrl" to currentUser.photoUrl.toString(),
                            "uid" to currentUser.uid
                        )

                        userDocRef.set(userMap)
                            .addOnSuccessListener {
                                Log.d("Firestore", "User successfully saved")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Failed to save user: ${e.message}")
                            }
                    }
                }
        }
    }

    override suspend fun uploadImageToStorage(imageUri: Uri): String {
        val storageRef =
            storage.reference.child("user_avatars/${Calendar.getInstance().timeInMillis}.jpg")
        storageRef.putFile(imageUri).await()
        return storageRef.downloadUrl.await().toString()
    }

    override suspend fun updateUserProfilePhoto(photoUrl: String) {
        val user = firebaseAuth.currentUser ?: return
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(Uri.parse(photoUrl))
            .build()

        user.updateProfile(profileUpdates).await()
        saveNewUserInFirebase()
    }

    override fun activateNotification(turnOn: Boolean) {
        if (turnOn) {
            FirebaseMessaging.getInstance().subscribeToTopic("notifications")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        throw Exception("Failed to enable notifications")
                    }
                }
        } else {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        throw Exception("Failed to disable notifications")
                    }
                }
        }
    }
}