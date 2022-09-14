package com.ivorysoftinc.kotlin.example.di

import com.ivorysoftinc.kotlin.example.repository.CardsRepository
import com.ivorysoftinc.kotlin.example.repository.NetworkRepository
import org.koin.dsl.module

/**
 * Koin module that provides Repositories.
 */
val repositoriesModule = module {
    single { CardsRepository(get(), get(), get()) }
    single { NetworkRepository() }
}
