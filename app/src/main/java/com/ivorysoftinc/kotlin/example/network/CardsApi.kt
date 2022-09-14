package com.ivorysoftinc.kotlin.example.network

import com.ivorysoftinc.kotlin.example.network.model.RootCardsResponse
import retrofit2.http.GET

/**
 * Interface representing Network API for working with Cards.
 */
interface CardsApi {

    @GET("test/home")
    suspend fun getCards(): RootCardsResponse
}
