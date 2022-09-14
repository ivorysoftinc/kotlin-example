package com.ivorysoftinc.kotlin.example.network

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.ivorysoftinc.kotlin.example.data.Card
import java.lang.reflect.Type

/**
 * Custom [JsonDeserializer] for [Card].
 * Needed for correct [Card] object mapping from JSON.
 */
class CardDeserializer : JsonDeserializer<Card?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Card? {
        val obj = json?.asJsonObject

        return when (obj?.get(CARD_TYPE_ATTR)?.asString) {
            Card.Text.CARD_TYPE -> Card.Text::class.java
            Card.TitleDescription.CARD_TYPE -> Card.TitleDescription::class.java
            Card.ImageTitleDescription.CARD_TYPE -> Card.ImageTitleDescription::class.java
            else -> null
        }?.let { clazz ->
            Gson().fromJson(obj?.get(CARD_ATTR), clazz)
        }
    }

    companion object {
        private const val CARD_TYPE_ATTR = "card_type"
        private const val CARD_ATTR = "card"
    }
}
