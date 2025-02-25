package fr.ilardi.eventorias.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import fr.ilardi.eventorias.model.Event
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * FirestoreEventRepository is used to connect to
 * Firebase Firestore for getting, uptading and uploading event
 */

class FirestoreEventRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) : IEventRepository {

    private val eventsCollection = firestore.collection("events")

    override fun getEventsCollection(): Flow<List<Event>> = callbackFlow {
        val subscription = eventsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }
            val events = snapshot?.toObjects(Event::class.java) ?: emptyList()
            trySend(events).isSuccess
        }

        awaitClose { subscription.remove() }
    }

    override fun getEventById(eventId: String): Flow<Event?> = callbackFlow {
        val query = eventsCollection.whereEqualTo("id", eventId).limit(1)
        val subscription = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                close(exception)
                return@addSnapshotListener
            }

            val event = snapshot?.documents?.firstOrNull()?.toObject(Event::class.java)
            trySend(event).isSuccess
        }

        awaitClose { subscription.remove() }
    }

    override suspend fun getEvents(): List<Event> {
        return try {
            val snapshot = eventsCollection.get().await()
            snapshot.toObjects(Event::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addEvent(event: Event) {
        val photo = uploadPhotoToFirestore(Uri.parse(event.image))
        val newEvent = event.copy(image = photo)
        eventsCollection.add(newEvent)
    }

    private suspend fun uploadPhotoToFirestore(imageUri: Uri): String {
        val storageRef = FirebaseStorage.getInstance().reference.child("images/${System.currentTimeMillis()}.jpg")

        return withContext(NonCancellable) {
            try {
                storageRef.putFile(imageUri).await()
                val downloadUrl = storageRef.downloadUrl.await()
                downloadUrl.toString()
            } catch (e: Exception) {
                throw Exception("Échec de l'upload de l'image : ${e.message}")
            }
        }
    }
}