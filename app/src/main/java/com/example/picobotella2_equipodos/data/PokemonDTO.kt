package com.example.picobotella2_equipodos.data

import com.google.gson.annotations.SerializedName

data class PokemonDTO(
    @SerializedName("img")
    val imagenUrl: String
)