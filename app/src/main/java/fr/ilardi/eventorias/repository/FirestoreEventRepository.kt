package fr.ilardi.eventorias.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import fr.ilardi.eventorias.model.Event
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreEventRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    //    private val eventFireBaseCollection = firestore.collection("events")
    private val eventsCollection = firestore.collection("events")

    fun getEventsCollection() = eventsCollection
    fun addEvent(event: Event, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        eventsCollection.add(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    suspend fun getEvents(): List<Event> {
        return try {
            val snapshot = eventsCollection.get().await()
            snapshot.toObjects(Event::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getEventById(eventId: String): Flow<Event?> = callbackFlow {
        try {
            val query = eventsCollection
                .whereEqualTo("id", eventId)
                .limit(1)

            val subscription = query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception)
                    return@addSnapshotListener
                }

                val event = snapshot?.documents?.firstOrNull()?.toObject(Event::class.java)
                trySend(event).isSuccess
            }

            awaitClose { subscription.remove() }
        } catch (e: Exception) {
            close(e)
        }
    }

    suspend fun addEvent(event: Event) {
        val photo = uploadPhotoToFirestore(Uri.parse(event.image))
        val newEvent = Event(
            image = photo,
            title = event.title,
            address = event.address,
            id=event.id,
            description = event.description,
            authorUid = event.authorUid
            )

        eventsCollection.add(newEvent)
    }

    private suspend fun uploadPhotoToFirestore(imageUri: Uri): String {
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("images/${System.currentTimeMillis()}.jpg")

        return try {
            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            downloadUrl.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Échec de l'upload de l'image : ${e.message}")
        }
    }
}