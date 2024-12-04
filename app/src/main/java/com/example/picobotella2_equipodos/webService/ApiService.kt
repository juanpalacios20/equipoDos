package com.example.picobotella2_equipodos.webService
import com.example.picobotella2_equipodos.model.Pokedex
import retrofit2.http.GET

interface PokemonApiService {
    @GET("Biuni/PokemonGO-Pokedex/master/pokedex.json")
    suspend fun getPokemonList(): Pokedex

}
