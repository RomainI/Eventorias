package fr.ilardi.eventorias.di

data class GooglePredictionsResponse(
    val predictions: ArrayList<GooglePrediction>
)

data class GooglePredictionTerm(
    val offset: Int,
    val value: String
)

data class GooglePrediction(
    val description: String,
    val terms: List<GooglePredictionTerm>
)