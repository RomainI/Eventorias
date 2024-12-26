package fr.ilardi.eventorias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.eventorias.ui.screens.CreateEventScreen
import fr.ilardi.eventorias.ui.screens.EventDetailScreen
import fr.ilardi.eventorias.ui.screens.EventListScreen
import fr.ilardi.eventorias.ui.screens.LoginScreen
import fr.ilardi.eventorias.ui.theme.EventoriasTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EventoriasTheme {
                EventoriasApp()
            }
        }
    }
}

@Composable
fun EventoriasApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        composable("event_list") {
            EventListScreen(
                onEventClick = {
                    navController.navigate("EventDetailScreen/${it.id}")
                },
                onFABClick = { navController.navigate("add_event") }
            )
        }
        composable("add_event") {
            CreateEventScreen(onBackClick = { navController.navigateUp() })
        }

        composable("event_detail/{eventId}") { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            if (eventId != null) {
                EventDetailScreen(eventId = eventId, onBackClick = { navController.navigateUp() })
            }
        }
//        composable("profile") {
//            ProfileScreen()
//        }

        composable("auth") {
            LoginScreen(onLoginAction = { navController.navigate("event_list") })
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EventoriasTheme {
        Greeting("Android")
    }
}