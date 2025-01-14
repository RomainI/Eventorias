package fr.ilardi.eventorias.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import fr.ilardi.eventorias.R
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.utils.AddressSearchField
import fr.ilardi.eventorias.viewmodel.EventViewModel
import fr.ilardi.eventorias.viewmodel.PredictionViewModel
import java.io.File
import java.io.FileOutputStream
import kotlin.math.round


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    onBackClick: () -> Unit,
    viewModel: EventViewModel = hiltViewModel(),
    predictionViewModel: PredictionViewModel = hiltViewModel()
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
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            date = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(java.util.Calendar.YEAR),
        calendar.get(java.util.Calendar.MONTH),
        calendar.get(java.util.Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            time = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(java.util.Calendar.HOUR_OF_DAY),
        calendar.get(java.util.Calendar.MINUTE),
        false
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Creation of an event", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
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
            // Partie "Date et Heure" avec Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Date Picker
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable { datePickerDialog.show() }
                    ) {
                        TextField(
                            value = date,
                            onValueChange = {},
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            label = { Text(text = "Date", color = Color.Gray) },
                            placeholder = { Text(text = "Select Date", color = Color.Gray) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp),
                            enabled = false // Pour empêcher la saisie manuelle
                        )
                    }
                }

                // Time Picker
                Column(modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = Color.DarkGray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clickable { timePickerDialog.show() }
                    ) {
                        TextField(
                            value = time,
                            onValueChange = {},
                            textStyle = LocalTextStyle.current.copy(color = Color.White),
                            label = { Text(text = "Time", color = Color.Gray) },
                            placeholder = { Text(text = "Select Time", color = Color.Gray) },
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(4.dp),
                            enabled = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**  Surface(
            shape = MaterialTheme.shapes.small,
            color = Color.DarkGray,
            modifier = Modifier
            .fillMaxWidth()

            ) {*/
            /** TextField(
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
            )*/

//                Text(
//                    text = "Address",
//                    color = Color.Gray,
//                    fontSize = 12.sp,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
            AddressSearchField(
                viewModel = predictionViewModel,
                onAddressSelected = { selectedAddress ->
                    address = selectedAddress // Met à jour la variable address
                }
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Buttons for Image Selection
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Camera Button
                IconButton(
                    onClick = { cameraLauncher.launch() },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.camera_alt),
                        contentDescription = "Take Photo",
                        tint = Color.Black

                    )
                }

                // Gallery Button
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Red)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.attach_file),
                        contentDescription = "Add Photo",


                        )
                }
            }

            Spacer(modifier = Modifier.height(46.dp))

            // Validate Button
            Button(
                onClick = {
                    Log.d("Event", title+" "+date+" "+description+" "+address)
                    if (title.isNotEmpty() && date.isNotEmpty() && description.isNotEmpty() && address.isNotEmpty()) {
                        val event = Event(
                            title = title,
                            description = description,
                            date = date,
                            time = time,
                            address = address,
                            image = selectedImageUri?.toString() ?: "",
                            authorUid = viewModel.getFirebaseUser()?.uid,
                            id = Calendar.getInstance().timeInMillis.toString()
                        )
                        viewModel.addEvent(event)
                        Toast.makeText(context, "Event added successfully", Toast.LENGTH_SHORT)
                            .show()
                        onBackClick()
                    } else {
                        Toast.makeText(context, "You must complete all fields", Toast.LENGTH_SHORT)
                            .show()
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
//}

fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val filename = "temp_image_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, filename)
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.close()
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
}