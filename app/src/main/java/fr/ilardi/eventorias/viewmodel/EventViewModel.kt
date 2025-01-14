package fr.ilardi.eventorias.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.model.User
import fr.ilardi.eventorias.repository.AuthenticationRepository
import fr.ilardi.eventorias.repository.FirestoreEventRepository
import fr.ilardi.eventorias.repository.GoogleStaticMapsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor (private val repository: FirestoreEventRepository, private val authenticationRepository: AuthenticationRepository, private val mapsRepository: GoogleStaticMapsRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events
    private val _userFlow = MutableStateFlow<User?>(null)
    val userFlow: StateFlow<User?> = _userFlow



    init {
        observeEvents()
    }

    private fun observeEvents() {
        repository.getEventsCollection().addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("EventViewModel", "Error events: ${exception.message}")
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val eventList = snapshot.toObjects(Event::class.java)
                    _events.value = eventList
                }
            }
    }

    suspend fun getMapWithAddress(address : String) : Bitmap? {
        return mapsRepository.fetchStaticMap(address)
    }

    fun getEventById(eventId: String): Flow<Event?> {
        return repository.getEventById(eventId)
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            repository.addEvent(event)
            Log.d("ADD EVENT ViewModel", event.title)
            _events.value = repository.getEvents()
        }
    }

    fun getAuthorName(): String? {
        return authenticationRepository.getCurrentUser()?.displayName
    }

    fun getAuthorURI(): Uri? {
        return authenticationRepository.getCurrentUser()?.photoUrl
    }

    fun getUserByUid(uid: String) {
        viewModelScope.launch {
            _userFlow.value = authenticationRepository.getUserByUid(uid)
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return authenticationRepository.getCurrentUser()
    }
}