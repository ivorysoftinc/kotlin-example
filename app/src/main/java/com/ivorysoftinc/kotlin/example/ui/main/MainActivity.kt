package com.ivorysoftinc.kotlin.example.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ivorysoftinc.kotlin.example.resources.theme.KotlinExampleTheme
import com.ivorysoftinc.kotlin.example.ui.screens.CardsScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KotlinExampleTheme {
                CardsScreen(viewModel)
            }
        }
    }
}
