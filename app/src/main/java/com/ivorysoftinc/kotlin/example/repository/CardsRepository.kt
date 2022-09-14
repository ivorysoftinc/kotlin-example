package com.ivorysoftinc.kotlin.example.repository

import com.ivorysoftinc.kotlin.example.cache.CardMapper
import com.ivorysoftinc.kotlin.example.cache.CardsDao
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.extensions.typeFlow
import com.ivorysoftinc.kotlin.example.network.CardsApi
import kotlinx.coroutines.flow.Flow

/**
 * Repository class for working with Network API and DB Cache.
 */
class CardsRepository(
    private val cardsApi: CardsApi,
    private val cardsDao: CardsDao,
    private val cardMapper: CardMapper
) {

    suspend fun getCards(): Flow<List<Card>> = typeFlow {
        cardsApi.getCards().page.cards.also {
            cardsDao.updateCards(it.mapNotNull(cardMapper::cardToEntity))
        }
    }

    suspend fun getCachedCards(): Flow<List<Card>> = typeFlow {
        cardsDao.getCards().mapNotNull(cardMapper::entityToCard)
    }
}
