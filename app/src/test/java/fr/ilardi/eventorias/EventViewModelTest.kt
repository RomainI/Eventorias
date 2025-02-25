package fr.ilardi.eventorias.viewmodel

import android.net.Uri
import androidx.core.net.toUri

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.model.User
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

        mockkStatic(Uri::class) // Mock de la classe Uri
        every { Uri.parse(any()) } answers { mockk() } // Simule Uri.parse()

        eventViewModel = EventViewModel(repository, authenticationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    

    @Test
    fun addEventTest() = runTest {
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
    fun getAuthorNameTest() {
        // Given
        val mockUser = mockk<User>(relaxed = true)
        every { mockUser.name } returns "John Doe"
        every { authenticationRepository.getCurrentUser() } returns mockUser

        // When
        val authorName = eventViewModel.getAuthorName()

        // Then
        assertEquals("John Doe", authorName)
    }

    @Test
    fun getEventByIdTest() = runTest {
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

    @Test
    fun updateUserByUidTest() = runTest {
        // Given
        val mockUser = User(uid = "123", name = "John Doe", email = "john@example.com", profileImage = "")
        coEvery { authenticationRepository.getUserByUid("123") } returns mockUser

        // When
        eventViewModel.updateUserByUid("123")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockUser, eventViewModel.userState.value)
    }

    @Test
    fun addEventOnExistingListTest() = runTest {
        // Given
        val initialEvent = Event(id = "1", title = "Event 1", address = "Address 1", image = "", description = "", authorUid = "", date = "", time = "")
        val newEvent = Event(id = "3", title = "Event 3", address = "Address 3", image = "", description = "", authorUid = "", date = "", time = "")
        coEvery { repository.getEvents() } returns listOf(initialEvent, newEvent)

        // When
        eventViewModel.addEvent(newEvent)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(listOf(initialEvent, newEvent), eventViewModel.events.value)
        coVerify { repository.addEvent(newEvent) }
    }

    @Test
    fun loadMapTest() {
        // Given
        val address = "7 rue de la chapelle"
        val fakeApiKey = "FAKE_API_KEY"

        // When
        val result = eventViewModel.loadMap(address, fakeApiKey)

        // Then
        val expected = "https://maps.googleapis.com/maps/api/staticmap?center=7 rue de la chapelle&zoom=15&size=200x100&key=FAKE_API_KEY&markers=size:mid&maptype=roadmap"
        assertEquals(expected, result)
    }
}