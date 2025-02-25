package fr.ilardi.eventorias.ui.screens

import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.ilardi.eventorias.model.Event
import fr.ilardi.eventorias.viewmodel.EventViewModel
import fr.ilardi.eventorias.R
import coil.compose.rememberAsyncImagePainter
import fr.ilardi.eventorias.model.User
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * EventListScreen displays a list of events and allows navigation
 * to event details or profile by clicking on it
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel = hiltViewModel(),
    onEventClick: (Event) -> Unit = {},
    onFABClick: () -> Unit = {}
) {
    val events by viewModel.events.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) } // Event tab selected by default
    var isSearching by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val filteredEvents = if (searchQuery.isNotEmpty()) {
        events.filter { it.title.contains(searchQuery, ignoreCase = true) }
    } else {
        events
    }
    var isSortedByNewestFirst by remember { mutableStateOf(true) }
    val sortedEvents = if (isSortedByNewestFirst) {
        filteredEvents.sortedByDescending { it.date }
    } else {
        filteredEvents.sortedBy { it.date }
    }

    Scaffold(
        topBar = {

            if (selectedTabIndex == 0)

                TopAppBar(
                    title = {
                        if (isSearching) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text(stringResource(R.string.search_events)) }
                            )
                        } else {
                            Text(text = stringResource(R.string.event_list), color = Color.White)
                        }
                    },
                    actions = {
                        if (isSearching) {
                            IconButton(onClick = {
                                isSearching = false
                                searchQuery = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Close Search",
                                    tint = Color.White
                                )
                            }
                        } else {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.search),
                                    contentDescription = "Search",
                                    tint = Color.White
                                )
                            }
                        }
                        IconButton(onClick = { isSortedByNewestFirst = !isSortedByNewestFirst }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.sort),
                                contentDescription = "Sort",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.background
                    )
                )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        label = {
                            Text(
                                text = stringResource(R.string.events),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                contentDescription = "Événements",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Gray,
                            selectedTextColor = Color.Gray,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = Color.Gray
                        )
                    )
                    NavigationBarItem(
                        modifier = Modifier.semantics { contentDescription = "Profil" } ,
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        label = { Text(stringResource(R.string.profile), color = MaterialTheme.colorScheme.onBackground) },
                        icon = {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = stringResource(R.string.profile),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Gray,
                            selectedTextColor = Color.Gray,
                            unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                            unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                            indicatorColor = Color.Gray
                        )
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { onFABClick() },
                    containerColor = Color.Red
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.add_fragment_label),
                        tint = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when (selectedTabIndex) {
                0 -> EventColumn(sortedEvents, Modifier.padding(16.dp), onEventClick, viewModel)
                1 -> ManagementScreen()
            }
        }
    }
}

@Composable
fun EventColumn(
    events: List<Event>,
    modifier: Modifier = Modifier,
    onEventClick: (Event) -> Unit,
    viewModel: EventViewModel
) {
    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
    ) {
        items(events) { event ->
            EventItem(
                event = event, onEventClick = onEventClick, viewModel = viewModel
            )
        }
    }
}


@Composable
fun EventItem(event: Event, onEventClick: (Event) -> Unit, viewModel: EventViewModel) {

    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(event.authorUid) {
        event.authorUid?.let {
            viewModel.updateUserByUid(it)
            val fetchedUser = viewModel.userState.value
            user = fetchedUser
        }
    }
    val imageUrl = user?.profileImage
        ?: "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg"

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = { onEventClick(event) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Author Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(25.dp)),
                contentScale = ContentScale.Crop
            )


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = convertDateToFullText(event.date),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Image(
                painter = rememberAsyncImagePainter(event.image),
                contentDescription = "Event Image",
                modifier = Modifier
                    .size(width = 85.dp, height = 55.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }
    }
}

fun convertDateToFullText(dateString: String): String {
    val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    val outputFormat = SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(dateString)

        if (date != null) outputFormat.format(date) else "Date invalide"
    } catch (e: Exception) {
        "Date invalide"
    }
}