package fr.ilardi.eventorias

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import fr.ilardi.eventorias.di.AppModule
import fr.ilardi.eventorias.ui.screens.LoginScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(AppModule::class)
@HiltAndroidTest
class LoginScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>() // Teste l'Activity complète

    @Before
    fun setUp() {
        hiltRule.inject()
        composeTestRule.waitForIdle()
    }

    @Test
    fun testLoginScreenDisplaysCorrectly() {
        composeTestRule.waitForIdle() // Attendre que l'UI soit prête

        // Vérifie qu'un élément avec le testTag "DisplayLoginScreen" est bien affiché
        composeTestRule.onNodeWithTag("DisplayLoginScreen").assertIsDisplayed()
    }
}