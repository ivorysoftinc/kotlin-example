package com.ivorysoftinc.kotlin.example.di

import android.content.Context
import androidx.room.Room
import com.ivorysoftinc.kotlin.example.cache.AppDatabase
import com.ivorysoftinc.kotlin.example.cache.CardMapper
import org.koin.dsl.module

/**
 * Koin module that provides common Application dependencies.
 */
val appModule = module {
    single { provideDatabase(get()) }
    single { provideCardsDao(get()) }
    single { CardMapper() }
}

private fun provideDatabase(applicationContext: Context): AppDatabase {
    return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "cards-db").build()
}

private fun provideCardsDao(db: AppDatabase) = db.cardsDao()
