package fr.ilardi.eventorias.ui.screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.viewmodel.EventViewModel
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onBackClick: () -> Unit,
    viewModel: EventViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToUri(context, bitmap)
            selectedImageUri = uri
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Creation of an event") },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title Input
//            Text(
//                text = "Title",
//                color = Color.Gray,
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    placeholder = { Text(text = "Enter title", color = Color.Gray) },
                    label = { Text(text = "Title", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description Input
//            Text(
//                text = "Description",
//                color = Color.Gray,
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    label = { Text(text = "Description", color = Color.Gray) },
                    placeholder = { Text(text = "Enter description", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date and Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
//                    Text(
//                        text = "Date",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        TextField(
                            value = date,
                            onValueChange = { date = it },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            label = { Text(text = "Date", color = Color.Gray) },
                            placeholder = { Text(text = "MM/DD/YYYY", color = Color.Gray) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = "Time",
//                        color = Color.Gray,
//                        fontSize = 12.sp
//                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        TextField(
                            value = time,
                            onValueChange = { time = it },
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            label = { Text(text = "Time", color = Color.Gray) },
                            placeholder = { Text(text = "HH:MM", color = Color.Gray) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Address Input
//            Text(
//                text = "Address",
//                color = Color.Gray,
//                fontSize = 12.sp,
//                modifier = Modifier.fillMaxWidth()
//            )
            Surface(
                shape = MaterialTheme.shapes.small,
                color = Color.DarkGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                TextField(
                    value = address,
                    onValueChange = { address = it },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    label = { Text(text = "Address", color = Color.Gray) },
                    placeholder = { Text(text = "Enter address", color = Color.Gray) },
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons for Image Selection
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Camera Button
                IconButton(
                    onClick = { cameraLauncher.launch() },
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.White)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Take Photo",
                        tint = Color.Transparent
                    )
                }

                // Gallery Button
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.Red)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.attach),
                        contentDescription = "Add Photo",
                        tint = Color.Transparent
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Validate Button
            Button(
                onClick = {
                    if (title.isNotEmpty() && date.isNotEmpty()) {
                        val event = Event(
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            address = address,
                            image = selectedImageUri?.toString() ?: ""
                        )
                        viewModel.addEvent(event)
                        Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "Title and Date are required", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Validate", color = Color.White)
            }
        }
    }
}

fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val filename = "temp_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, filename)
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}