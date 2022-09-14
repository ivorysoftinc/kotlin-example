package com.ivorysoftinc.kotlin.example

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ivorysoftinc.kotlin.example.resources.strings.NO_INTERNET_CONNECTION
import com.ivorysoftinc.kotlin.example.resources.theme.KotlinExampleTheme
import com.ivorysoftinc.kotlin.example.ui.main.MainViewModel
import com.ivorysoftinc.kotlin.example.ui.screens.CardsScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.getStateViewModel

/**
 * Instrumented tests.
 */
@RunWith(AndroidJUnit4::class)
class ComposeInstrumentedTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        // Start the app
        composeTestRule.setContent {
            KotlinExampleTheme {
                val viewModel = getStateViewModel<MainViewModel>()

                CardsScreen(viewModel)
            }
        }
    }

    /**
     * Test showing and hiding progress.
     */
    @Test
    fun cardsScreenProgressTest() = runBlocking {
        // Check is progress initially displayed
        composeTestRule.onNode(hasTestTag("Progress")).assertIsDisplayed()

        // Wait 10 seconds
        delay(5_000L)

        // Check is progress not displayed
        composeTestRule.onNode(hasTestTag("Progress")).assertDoesNotExist()
    }

    /**
     * Test refreshing data.
     */
    @Test
    fun cardsScreenRefreshTest() = runBlocking {
        // Wait for initial load
        cardsScreenProgressTest()

        // Click refresh
        composeTestRule.onNode(hasClickAction()).performClick()

        // Check is showing progress
        cardsScreenProgressTest()
    }

    /**
     * Test no connection.
     */
    @Test
    fun cardsScreenNoConnectionTest() = runBlocking {
        // Check is no connection text not displayed
        composeTestRule.onNode(hasText(NO_INTERNET_CONNECTION)).assertDoesNotExist()

        delay(10_000L)

        // Important!
        // Here you need to manually turn off network on tested device

        // Check is no connection text displayed
        composeTestRule.onNode(hasText(NO_INTERNET_CONNECTION)).assertIsDisplayed()

        Unit
    }
}
