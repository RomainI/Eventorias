package fr.ilardi.eventorias

import fr.ilardi.eventorias.di.GooglePrediction
import fr.ilardi.eventorias.di.GooglePredictionTerm
import fr.ilardi.eventorias.di.GooglePredictionsResponse
import fr.ilardi.eventorias.repository.GooglePlacesRepository
import fr.ilardi.eventorias.utils.Resource
import fr.ilardi.eventorias.viewmodel.PredictionViewModel
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PredictionViewModelTest {

    private lateinit var viewModel: PredictionViewModel
    private val googleRepository: GooglePlacesRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PredictionViewModel(googleRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun updatePredictionTest() = runTest {
        // Given
        val address = "7 rue de la chapelle"

        val mockPredictions = arrayListOf(
            GooglePrediction(
                description = "Paris, France",
                terms = listOf(GooglePredictionTerm(0, "Paris"), GooglePredictionTerm(7, "France"))
            ),
            GooglePrediction(
                description = "Lyon, France",
                terms = listOf(GooglePredictionTerm(0, "Lyon"), GooglePredictionTerm(5, "France"))
            )
        )

        val mockResponse = GooglePredictionsResponse(predictions = mockPredictions)

        coEvery { googleRepository.getPredictions(address) } returns Resource.Success(data = mockResponse)

        // When
        viewModel.updatePredictions(address)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(mockPredictions, viewModel.predictions.value)
        coVerify { googleRepository.getPredictions(address) }
    }

    @Test
    fun isLoadingBooleanTest() = runTest {
        val address = "7 rue de la chapelle"
        val mockPredictions = arrayListOf(
            GooglePrediction(
                description = "Paris, France",
                terms = listOf(GooglePredictionTerm(0, "Paris"), GooglePredictionTerm(7, "France"))
            )
        )
        val mockResponse = GooglePredictionsResponse(predictions = mockPredictions)
        coEvery { googleRepository.getPredictions(address) } returns Resource.Success(data = mockResponse)

        assertEquals(false, viewModel.isLoading.value)
    }

    @Test
    fun errorTest() = runTest {
        // Given
        val address = "Invalid address"
        coEvery { googleRepository.getPredictions(address) } returns Resource.Error(message = "Error fetching predictions")

        // When
        viewModel.updatePredictions(address)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        assertEquals(emptyList<GooglePrediction>(), viewModel.predictions.value)
        coVerify { googleRepository.getPredictions(address) }
    }

    @Test
    fun onSearchAddressTest() {
        // Given
        val address = "7 rue de la chapelle"
        val spyViewModel = spyk(viewModel, recordPrivateCalls = true)

        every { spyViewModel.updatePredictions(address) } just Runs

        // When
        spyViewModel.onSearchAddressChange(address)

        // Then
        verify { spyViewModel.updatePredictions(address) }
    }
}