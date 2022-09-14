package com.ivorysoftinc.kotlin.example.data

import com.google.gson.annotations.SerializedName

/**
 * Base class for representing data from API.
 */
sealed class Card {

    data class Text(
        @SerializedName("value") val value: String? = null,
        @SerializedName("attributes") val attributes: Attributes? = null
    ) : Card() {

        companion object {
            const val CARD_TYPE = "text"
        }
    }

    data class TitleDescription(
        @SerializedName("title") val title: Text? = null,
        @SerializedName("description") val description: Text? = null
    ) : Card() {

        companion object {
            const val CARD_TYPE = "title_description"
        }
    }

    data class ImageTitleDescription(
        @SerializedName("title") val title: Text? = null,
        @SerializedName("description") val description: Text? = null,
        @SerializedName("image") val image: Image? = null
    ) : Card() {

        companion object {
            const val CARD_TYPE = "image_title_description"
        }
    }
}

data class Attributes(
    @SerializedName("text_color") val textColor: String?,
    @SerializedName("font") val font: FontAttribute?
)

data class FontAttribute(@SerializedName("size") val size: Int?)

data class Image(
    @SerializedName("url") val url: String?,
    @SerializedName("size") val size: Size?
)

data class Size(
    @SerializedName("width") val width: Int?,
    @SerializedName("height") val height: Int?
)
