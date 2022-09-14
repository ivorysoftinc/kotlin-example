package com.ivorysoftinc.kotlin.example.network.model

import com.google.gson.annotations.SerializedName

data class RootCardsResponse(@SerializedName("page") val page: PageResponse)
