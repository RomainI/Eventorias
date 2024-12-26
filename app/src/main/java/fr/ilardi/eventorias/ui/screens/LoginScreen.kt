package fr.ilardi.eventorias.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser
import fr.ilardi.eventorias.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onLoginAction: () -> Unit,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {

    //    val context = LocalContext.current
//    var user by remember { mutableStateOf<FirebaseUser?>(null) }

    val authLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onLoginAction()
        }
    }

    LaunchedEffect(Unit) {
        val loginIntent = loginViewModel.getLoginIntent()
        authLauncher.launch(loginIntent)
    }
}