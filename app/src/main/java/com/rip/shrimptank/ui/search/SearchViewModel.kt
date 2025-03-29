package com.rip.shrimptank.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimal
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimalServiceClient

class SearchViewModel : ViewModel() {
    var aquaticAnimals: MutableLiveData<MutableList<AquaticAnimal>> = MutableLiveData()

    fun clearAquaticAnimals() {
        aquaticAnimals.postValue(mutableListOf())
    }

    fun refreshAquaticAnimals(query: String) {
        AquaticAnimalServiceClient.instance.searchAquaticAnimals(query) {
            aquaticAnimals.postValue(it)
        }
    }
}