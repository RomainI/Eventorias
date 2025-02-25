package fr.ilardi.eventorias.repository

import fr.ilardi.eventorias.di.GooglePrediction
import fr.ilardi.eventorias.di.GooglePredictionTerm
import fr.ilardi.eventorias.di.GooglePredictionsResponse
import fr.ilardi.eventorias.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * FakeGooglePlacesRepository is a GooglePlacesRepository mock for testing purpose
 */


class FakeGooglePlacesRepository: IGooglePlacesRepository {

    private val fakePredictions = MutableStateFlow(
        GooglePredictionsResponse(
            predictions = arrayListOf(
                GooglePrediction(
                    description = "10 Rue de Rivoli, Paris, France",
                    terms = listOf(GooglePredictionTerm(0, "10 Rue de Rivoli"), GooglePredictionTerm(15, "Paris"), GooglePredictionTerm(21, "France"))
                ),
                GooglePrediction(
                    description = "Tour Eiffel, Paris, France",
                    terms = listOf(GooglePredictionTerm(0, "Tour Eiffel"), GooglePredictionTerm(13, "Paris"), GooglePredictionTerm(19, "France"))
                )
            )
        )
    )

    override suspend fun getPredictions(input: String): Resource<GooglePredictionsResponse> {
        return if (input.isNotEmpty()) {
            Resource.Success(fakePredictions.value)
        } else {
            Resource.Error("Failed prediction")
        }
    }

}