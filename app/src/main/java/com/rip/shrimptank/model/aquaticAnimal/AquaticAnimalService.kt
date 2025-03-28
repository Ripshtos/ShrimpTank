package com.rip.shrimptank.model.aquaticAnimal

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AquaticAnimalService {
    @GET("aquarium")
    fun searchAquaticAnimals(@Query("query") query: String, ): Call<AquaticAnimalApiResponse>
}
