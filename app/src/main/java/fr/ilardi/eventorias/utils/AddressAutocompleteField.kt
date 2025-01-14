package fr.ilardi.eventorias.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import fr.ilardi.eventorias.di.GooglePrediction
import fr.ilardi.eventorias.viewmodel.PredictionViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressSearchField(viewModel: PredictionViewModel, onAddressSelected: (String) -> Unit) {
    val address = remember { mutableStateOf("") }
    val predictions = viewModel.predictions.value
    val isLoading = viewModel.isLoading.value




    Column {
        TextField(
            value = address.value,
            onValueChange = { newAddress : String ->
                address.value = newAddress
                if (newAddress.length >= 6) {
                    viewModel.onSearchAddressChange(newAddress)
                }
            },
            textStyle = LocalTextStyle.current.copy(color = Color.White),

            placeholder =  {Text(text = "Enter address", color = Color.Gray) },
            label =  {Text(text ="Address", color = Color.Gray )},
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.DarkGray,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(8.dp),
            enabled = true
        )

        if (isLoading) {
            Text("Loading suggestions...", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }

        // Suggestions List
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(predictions) { prediction ->
                PredictionItem(
                    prediction = prediction,
                    onClick = { selectedAddress ->
                        address.value = selectedAddress
                        onAddressSelected(selectedAddress)
                        viewModel.onSearchAddressChange(selectedAddress)
                    }
                )
            }
        }
    }
}

@Composable
fun PredictionItem(prediction: GooglePrediction, onClick: (String) -> Unit) {
    Text(
        text = prediction.description,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(prediction.description) }
            .padding(8.dp),
        style = MaterialTheme.typography.bodyMedium
    )
}