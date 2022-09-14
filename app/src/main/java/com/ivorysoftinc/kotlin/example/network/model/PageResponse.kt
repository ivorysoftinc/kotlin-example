package com.ivorysoftinc.kotlin.example.network.model

import com.google.gson.annotations.SerializedName
import com.ivorysoftinc.kotlin.example.data.Card

data class PageResponse(@SerializedName("cards") val cards: List<Card>)
