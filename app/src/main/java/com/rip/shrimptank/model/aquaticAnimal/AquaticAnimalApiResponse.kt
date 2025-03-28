package com.rip.shrimptank.model.aquaticAnimal

data class AquaticAnimalApiResponse(
    val results: Array<AquaticAnimal>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AquaticAnimalApiResponse

        return results.contentEquals(other.results)
    }

    override fun hashCode(): Int {
        return results.contentHashCode()
    }
}