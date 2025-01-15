package fr.ilardi.eventorias.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.repository.AuthenticationRepository
import fr.ilardi.eventorias.repository.FirestoreEventRepository
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var eventViewModel: EventViewModel
    private val repository: FirestoreEventRepository = mockk(relaxed = true)
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        eventViewModel = EventViewModel(repository, authenticationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    

    @Test
    fun `addEvent should update events list`() = runTest {
        // Given
        val newEvent = Event(id = "3", title = "Event 3", address = "Address 3", image = "", description = "", authorUid = "", date = "", time = "")
        coEvery { repository.addEvent(newEvent) } just Runs
        coEvery { repository.getEvents() } returns listOf(newEvent)

        // When
        eventViewModel.addEvent(newEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(listOf(newEvent), eventViewModel.events.value)
        coVerify { repository.addEvent(newEvent) }
    }

    @Test
    fun `getAuthorName should return current user's display name`() {
        // Given
        val mockUser = mockk<FirebaseUser>(relaxed = true)
        every { mockUser.displayName } returns "John Doe"
        every { authenticationRepository.getCurrentUser() } returns mockUser

        // When
        val authorName = eventViewModel.getAuthorName()

        // Then
        assertEquals("John Doe", authorName)
    }

    @Test
    fun `getEventById should return correct event`() = runTest {
        // Given
        val mockEvent = Event(id = "1", title = "Event 1", address = "Address 1", image = "", description = "", authorUid = "", date = "", time = "")
        every { repository.getEventById("1") } returns flow { emit(mockEvent) }

        // When
        val eventFlow = eventViewModel.getEventById("1")

        // Then
        eventFlow.collect { event ->
            assertEquals(mockEvent, event)
        }
    }
}