package com.ivorysoftinc.kotlin.example.network

import com.ivorysoftinc.kotlin.example.network.model.RootCardsResponse
import retrofit2.http.GET

/**
 * Interface representing Network API for working with Cards.
 */
interface CardsApi {

    @GET("ivorysoftinc/kotlin-example/master/list_data.json")
    suspend fun getCards(): RootCardsResponse
}
