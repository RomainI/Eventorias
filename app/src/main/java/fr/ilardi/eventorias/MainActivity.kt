package fr.ilardi.eventorias

import android.app.VoiceInteractor
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.squareup.okhttp.OkHttpClient
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.eventorias.ui.screens.CreateEventScreen
import fr.ilardi.eventorias.ui.screens.EventDetailScreen
import fr.ilardi.eventorias.ui.screens.EventListScreen
import fr.ilardi.eventorias.ui.screens.LoginScreen
import fr.ilardi.eventorias.ui.theme.EventoriasTheme
import org.json.JSONObject

/**
 * MainActivity is the entry point of the application.
 * It initializes the Places API and manages screen routes.
 */


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        }

        val isTestMode = BuildConfig.DEBUG


        setContent {
            EventoriasTheme {
                EventoriasApp(isTestMode)
            }
        }
    }

}

@Composable
fun EventoriasApp(isTestMode: Boolean) {
    val navController = rememberNavController()
    val startDestination = if (isTestMode) "event_list" else "auth"
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("event_list") {
            EventListScreen(
                onEventClick = {
                    navController.navigate("event_detail/${it.id}")
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