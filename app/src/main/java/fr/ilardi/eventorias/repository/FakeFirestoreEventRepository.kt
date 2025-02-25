package fr.ilardi.eventorias.repository

import fr.ilardi.eventorias.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

/**
 * FakeFirestoreRepository is a FirestoreEventRepository mock for testing purpose
 */

class FakeFirestoreEventRepository : IEventRepository {

    private val fakeEvents = MutableStateFlow(mutableListOf<Event>())

    override fun getEventsCollection(): Flow<List<Event>> = fakeEvents

    override fun getEventById(eventId: String): Flow<Event?> = flow {
        emit(fakeEvents.value.find { it.id == eventId })
    }

    override suspend fun getEvents(): List<Event> = fakeEvents.value

    override suspend fun addEvent(event: Event) {
        val newEvents = fakeEvents.value.toMutableList()
        newEvents.add(event)
        fakeEvents.value = newEvents
    }
}