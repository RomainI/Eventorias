package fr.ilardi.eventorias

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import fr.ilardi.eventorias.model.User
import fr.ilardi.eventorias.repository.AuthenticationRepository
import fr.ilardi.eventorias.viewmodel.ManagementViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ManagementViewModelTest {

    private lateinit var viewModel: ManagementViewModel
    private val authenticationRepository: AuthenticationRepository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ManagementViewModel(authenticationRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun userInitializedInAuthenticationRepositoryTest() = runTest {
        // Given
        val mockUser = mockk<User>()
        every { authenticationRepository.getCurrentUser() } returns mockUser

        // When
        val testViewModel = ManagementViewModel(authenticationRepository)

        // Then
        assertEquals(mockUser, testViewModel.user.first())
    }

    @Test
    fun uploadImageAndUpdateProfileTest() = runTest {
        // Given
        val mockUri = mockk<Uri>()
        val mockDownloadUrl = "https://fake-url.com/image.jpg"
        val mockUser = mockk<User>()

        coEvery { authenticationRepository.uploadImageToStorage(mockUri) } returns mockDownloadUrl
        coEvery { authenticationRepository.updateUserProfilePhoto(mockDownloadUrl) } just Runs
        every { authenticationRepository.getCurrentUser() } returns mockUser

        // When
        viewModel.uploadImageAndUpdateProfile(mockUri)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockUser, viewModel.user.first())
        coVerify { authenticationRepository.uploadImageToStorage(mockUri) }
        coVerify { authenticationRepository.updateUserProfilePhoto(mockDownloadUrl) }
        coVerify { authenticationRepository.getCurrentUser() }
    }

    @Test
    fun switchNotificationTest() = runTest {
        // Given
        val turnOn = true
        coEvery { authenticationRepository.activateNotification(turnOn) } just Runs

        // When
        viewModel.switchNotification(turnOn)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { authenticationRepository.activateNotification(turnOn) }
    }
}