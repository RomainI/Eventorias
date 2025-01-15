package fr.ilardi.eventorias.viewmodel

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.BuildConfig
import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.model.User
import fr.ilardi.eventorias.repository.AuthenticationRepository
import fr.ilardi.eventorias.repository.FirestoreEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val repository: FirestoreEventRepository,
    private val authenticationRepository: AuthenticationRepository,
//    private val mapsRepository: GoogleStaticMapsRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events
//    private val _userFlow = MutableStateFlow<User?>(null)
//    val userFlow: StateFlow<User?> = _userFlow

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState


    init {
        authenticationRepository.saveNewUserInFirebase()
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




    fun loadMap(address: String): String{
        val mapUrlInitial = "https://maps.googleapis.com/maps/api/staticmap?center="
        val mapUrlProperties = "&zoom=15&size=200x100"
        val key = "&key="+BuildConfig.MAPS_API_KEY
        val mapUrlMapType = "&markers=size:mid&maptype=roadmap"
        return mapUrlInitial + address + mapUrlProperties + key + mapUrlMapType

    }

    fun getEventById(eventId: String): Flow<Event?> {
        return repository.getEventById(eventId)
    }

    fun addEvent(event: Event) {
        viewModelScope.launch {
            repository.addEvent(event)
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
            val user = authenticationRepository.getUserByUid(uid)
            _userState.value = user
        }
    }

    fun getFirebaseUser(): FirebaseUser? {
        return authenticationRepository.getCurrentUser()
    }



}