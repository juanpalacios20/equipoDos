package com.example.picobotella2_equipodos.webservice

import com.example.picobotella2_equipodos.data.PokemonResponse
import com.example.picobotella2_equipodos.utils.AppConstants.END_POINT
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET(END_POINT)
    suspend fun getPokemons(): Response<PokemonResponse>
}
