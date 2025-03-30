package com.rip.shrimptank.model.aquaticAnimal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AquaticAnimal(
    val name: String,
    @SerializedName("type_of_aquarium")
    val aquariumType: String,
    val info: String
) : Serializable