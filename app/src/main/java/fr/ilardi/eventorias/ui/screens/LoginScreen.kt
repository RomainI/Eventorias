package fr.ilardi.eventorias.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.auth.FirebaseAuth
import fr.ilardi.eventorias.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginAction: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
    firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance() // Injecté via Hilt en test
) {
    Box(modifier = Modifier.testTag("DisplayLoginScreen"))

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onLoginAction()
        }
    }

    LaunchedEffect(Unit) {
        if (firebaseAuth.currentUser == null) { // Firebase ne doit pas bloquer l'affichage
            val loginIntent = loginViewModel.getLoginIntent()
            authLauncher.launch(loginIntent)
        }
    }
}