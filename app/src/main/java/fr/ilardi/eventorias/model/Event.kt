package fr.ilardi.eventorias.model

import android.net.Uri
import com.google.firebase.auth.FirebaseUser

data class Event(
    val title: String = "",
    val description: String = "",
    val authorUid: String? = "",
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val id: String ="",
    val image: String=""
)