package fr.ilardi.eventorias.repository

import fr.ilardi.eventorias.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * IEventRepository defines an interface for event management.
 * It takes place to allow testing by using a fake AppModule
 */
interface IEventRepository {
    fun getEventsCollection(): Flow<List<Event>>
    fun getEventById(eventId: String): Flow<Event?>
    suspend fun getEvents(): List<Event>
    suspend fun addEvent(event: Event)
}