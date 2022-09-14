package com.ivorysoftinc.kotlin.example.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * [CardEntity] represents all Card subtypes.
 */
@Entity(tableName = CardsDao.TABLE_NAME)
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "cardType") val cardType: String,
    @ColumnInfo(name = "titleValue") val titleValue: String? = null,
    @ColumnInfo(name = "titleTextColor") val titleTextColor: String? = null,
    @ColumnInfo(name = "titleFontSize") val titleFontSize: Int? = null,
    @ColumnInfo(name = "descriptionValue") val descriptionValue: String? = null,
    @ColumnInfo(name = "descriptionTextColor") val descriptionTextColor: String? = null,
    @ColumnInfo(name = "descriptionFontSize") val descriptionFontSize: Int? = null,
    @ColumnInfo(name = "imageUrl") val imageUrl: String? = null,
    @ColumnInfo(name = "imageWidth") val imageWidth: Int? = null,
    @ColumnInfo(name = "imageHeight") val imageHeight: Int? = null
)
