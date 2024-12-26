package fr.ilardi.eventorias.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    viewModel: EventViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    eventId: String?
) {

    if (eventId == null) {
        Text(text = "Error")
        return
    }

    val event =
        viewModel.getEventById(eventId).collectAsStateWithLifecycle(initialValue = null).value


    if (event != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(event.title)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.contentDescription_go_back)
                            )
                        }
                    }
                )
            }

        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(contentPadding),
            ) {
                Text(text = event.description)
            }
        }
    }
}