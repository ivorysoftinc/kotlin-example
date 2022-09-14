package com.ivorysoftinc.kotlin.example

import android.app.Application
import com.ivorysoftinc.kotlin.example.di.appModule
import com.ivorysoftinc.kotlin.example.di.networkModule
import com.ivorysoftinc.kotlin.example.di.repositoriesModule
import com.ivorysoftinc.kotlin.example.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

/**
 * Custom [Application] class to initialize and start Koin.
 */
class TestApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(appModule, networkModule, repositoriesModule, viewModelsModule)
        }
    }
}
