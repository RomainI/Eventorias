package fr.ilardi.eventorias.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.repository.FirestoreEventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor (private val repository: FirestoreEventRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    init {
        viewModelScope.launch {
            _events.value = repository.getEvents()
        }
    }

    fun getEventById(eventId: String): Flow<Event?> {
        return repository.getEventById(eventId)
    }

    fun addEvent(event : Event){
        repository.addEvent(event)
    }
}