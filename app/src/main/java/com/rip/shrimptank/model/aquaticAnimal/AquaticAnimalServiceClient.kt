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
        .baseUrl("https://tsabar.pro/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val aquaticAnimalService = retrofit.create(AquaticAnimalService::class.java)

    fun searchAquaticAnimals(
        query: String,
        callback: (MutableList<AquaticAnimal>) -> Unit
    ) {

        val encodedSearch = URLEncoder.encode(query, "UTF-8")
        val searchAnimals = aquaticAnimalService.searchAquaticAnimals(encodedSearch)

        searchAnimals.enqueue(object : Callback<List<AquaticAnimal>> {
            override fun onResponse(
                call: Call<List<AquaticAnimal>>,
                response: Response<List<AquaticAnimal>>
            ) {
                if (response.isSuccessful) {
                    val animalList = response.body()
                    callback(animalList as? MutableList<AquaticAnimal> ?: mutableListOf())
                } else {
                    throw Exception("Failed to fetch aquatic animals")
                }
            }

            override fun onFailure(call: Call<List<AquaticAnimal>>, t: Throwable) {
                throw t
            }
        })
    }
}