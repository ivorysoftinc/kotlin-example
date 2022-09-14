package com.ivorysoftinc.kotlin.example.di

import com.ivorysoftinc.kotlin.example.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin module that provides ViewModels.
 */
val viewModelsModule = module {
    viewModel { MainViewModel(get(), get()) }
}
