package com.rip.shrimptank.model.aquaticAnimal

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URLEncoder

class AquaticAnimalServiceClient private constructor() {

    companion object {
        val instance: AquaticAnimalServiceClient = AquaticAnimalServiceClient()
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://node31.cs.colman.ac.il/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val aquaticAnimalService = retrofit.create(AquaticAnimalService::class.java)

    fun searchAquaticAnimals(
        query: String,
        callback: (MutableList<AquaticAnimal>) -> Unit
    ) {

        val encodedSearch = URLEncoder.encode(query, "UTF-8")
        val searchAnimals = aquaticAnimalService.searchAquaticAnimals(encodedSearch)

        searchAnimals.enqueue(object : Callback<AquaticAnimalApiResponse> {
            override fun onResponse(
                call: Call<AquaticAnimalApiResponse>,
                response: Response<AquaticAnimalApiResponse>
            ) {
                if (response.isSuccessful) {
                    val animalList = response.body()?.results
                    callback(animalList as? MutableList<AquaticAnimal> ?: mutableListOf())
                } else {
                    throw Exception("Failed to fetch aquatic animals")
                }
            }

            override fun onFailure(call: Call<AquaticAnimalApiResponse>, t: Throwable) {
                throw t
            }
        })
    }
}