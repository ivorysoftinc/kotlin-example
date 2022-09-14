package com.ivorysoftinc.kotlin.example.cache

import com.ivorysoftinc.kotlin.example.data.Attributes
import com.ivorysoftinc.kotlin.example.data.Card
import com.ivorysoftinc.kotlin.example.data.FontAttribute
import com.ivorysoftinc.kotlin.example.data.Image
import com.ivorysoftinc.kotlin.example.data.Size

/**
 * Mapper for converting [Card] to [CardEntity] and and vice versa.
 */
class CardMapper {

    fun cardToEntity(card: Card?): CardEntity? = card?.run {
        when (this) {
            is Card.Text -> CardEntity(
                cardType = Card.Text.CARD_TYPE,
                titleValue = value,
                titleFontSize = attributes?.font?.size,
                titleTextColor = attributes?.textColor
            )
            is Card.TitleDescription -> CardEntity(
                cardType = Card.TitleDescription.CARD_TYPE,
                titleValue = title?.value,
                titleFontSize = title?.attributes?.font?.size,
                titleTextColor = title?.attributes?.textColor,
                descriptionValue = description?.value,
                descriptionFontSize = description?.attributes?.font?.size,
                descriptionTextColor = description?.attributes?.textColor
            )
            is Card.ImageTitleDescription -> CardEntity(
                cardType = Card.ImageTitleDescription.CARD_TYPE,
                titleValue = title?.value,
                titleFontSize = title?.attributes?.font?.size,
                titleTextColor = title?.attributes?.textColor,
                descriptionValue = description?.value,
                descriptionFontSize = description?.attributes?.font?.size,
                descriptionTextColor = description?.attributes?.textColor,
                imageUrl = image?.url,
                imageWidth = image?.size?.width,
                imageHeight = image?.size?.height
            )
        }
    }

    fun entityToCard(entity: CardEntity?): Card? = entity?.run {
        return when (cardType) {
            Card.Text.CARD_TYPE -> Card.Text(
                value = titleValue,
                attributes = Attributes(
                    textColor = titleTextColor,
                    font = FontAttribute(size = titleFontSize)
                )
            )
            Card.TitleDescription.CARD_TYPE -> Card.TitleDescription(
                title = Card.Text(
                    value = titleValue,
                    attributes = Attributes(
                        textColor = titleTextColor,
                        font = FontAttribute(size = titleFontSize)
                    )
                ),
                description = Card.Text(
                    value = descriptionValue,
                    attributes = Attributes(
                        textColor = descriptionTextColor,
                        font = FontAttribute(size = descriptionFontSize)
                    )
                )
            )
            Card.ImageTitleDescription.CARD_TYPE -> Card.ImageTitleDescription(
                title = Card.Text(
                    value = titleValue,
                    attributes = Attributes(
                        textColor = titleTextColor,
                        font = FontAttribute(size = titleFontSize)
                    )
                ),
                description = Card.Text(
                    value = descriptionValue,
                    attributes = Attributes(
                        textColor = descriptionTextColor,
                        font = FontAttribute(size = descriptionFontSize)
                    )
                ),
                image = Image(url = imageUrl, size = Size(width = imageWidth, height = imageHeight))
            )
            else -> null
        }
    }
}
