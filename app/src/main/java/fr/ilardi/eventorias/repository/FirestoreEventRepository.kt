package fr.ilardi.eventorias.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import fr.ilardi.eventorias.model.Event
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreEventRepository @Inject constructor(
    private val firestore : FirebaseFirestore
) {

    private val eventFireBaseCollection = firestore.collection("events")

    fun addEvent(event: Event, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        eventFireBaseCollection.add(event)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    suspend fun getEvents(): List<Event> {
        return try {
            val snapshot = eventFireBaseCollection.get().await()
            snapshot.toObjects(Event::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getEventById(eventId: String): Flow<Event?> = callbackFlow {
        try {
            val query = eventFireBaseCollection
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

    fun addEvent(event: Event) {}
}