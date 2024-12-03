package com.example.picobotella2_equipodos.data

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    @SerializedName("pokemon")
    val pokemonList: List<PokemonDTO>?
)