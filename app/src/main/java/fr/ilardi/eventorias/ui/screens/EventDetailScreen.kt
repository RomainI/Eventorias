package fr.ilardi.eventorias.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.viewmodel.EventViewModel
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter


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
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }


    val event =
        viewModel.getEventById(eventId).collectAsStateWithLifecycle(initialValue = null).value


    if (event != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = event.title, color = Color.White)
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(id = R.string.contentDescription_go_back),
                                tint = Color.White
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
                Image(
                    painter = rememberAsyncImagePainter(event.image),
                    contentDescription = "Event Image",
                    modifier = Modifier
                        .size(width = 364.dp, height = 364.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(text = event.description)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(text = event.address)
                    LaunchedEffect(event.address) {
                        bitmap = viewModel.getMapWithAddress(event.address)
                    }

                    bitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Map Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentScale = ContentScale.Crop
                        )
                    } ?: Text(text = "Loading map...")
                }
            }

        }
    }
}
