package fr.ilardi.eventorias.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.ilardi.eventorias.MainActivity
import fr.ilardi.eventorias.repository.FakeAuthenticationRepository
import fr.ilardi.eventorias.repository.FakeFirestoreEventRepository
import fr.ilardi.eventorias.repository.IAuthenticationRepository
import fr.ilardi.eventorias.repository.IEventRepository
import fr.ilardi.eventorias.viewmodel.EventViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class EventDetailScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeAuthRepository: IAuthenticationRepository

    @Inject
    lateinit var fakeEventRepository: IEventRepository

    private lateinit var viewModel: EventViewModel

    @Before
    fun setup() {
        hiltRule.inject()

        viewModel = EventViewModel(fakeEventRepository, fakeAuthRepository)

        (fakeEventRepository as FakeFirestoreEventRepository).apply {
            addFakeEvent("1", "Soiree", "12/06/2025")
            addFakeEvent("2", "Match de Foot", "15/07/2025")
        }

        composeTestRule.waitForIdle()
    }

    @Test
    fun eventDetailScreen_displaysEventDetailsCorrectly() {
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Soiree").assertIsDisplayed()

        composeTestRule.onNodeWithText("Soiree").performClick()

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Soiree").assertIsDisplayed()
        composeTestRule.onNodeWithText("12/06/2025").assertIsDisplayed()
        composeTestRule.onNodeWithText("Description de Soiree").assertIsDisplayed()
        composeTestRule.onNodeWithText("Adresse de Soiree").assertIsDisplayed()
    }

    private fun FakeFirestoreEventRepository.addFakeEvent(id: String, title: String, date: String) {
        runBlocking {
            addEvent(
                fr.ilardi.eventorias.model.Event(
                    id = id,
                    title = title,
                    description = "Description de $title",
                    image = "https://example.com/$title.jpg",
                    address = "Adresse de $title",
                    authorUid = "123",
                    date = date,
                    time = "20:00"
                )
            )
        }
    }
}