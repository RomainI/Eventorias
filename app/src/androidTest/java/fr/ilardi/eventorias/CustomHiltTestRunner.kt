package fr.ilardi.eventorias
import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class CustomHiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        // Utilise l'application de test de Hilt
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}