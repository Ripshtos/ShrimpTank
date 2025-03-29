package com.rip.shrimptank.ui.aquaticAnimal

import androidx.lifecycle.ViewModel
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimal

class AquaticAnimalViewModel : ViewModel() {
    var aquaticAnimalDetailsData: AquaticAnimal? = null

    fun setAquaticAnimalDetails(aquaticAnimal: AquaticAnimal) {
        aquaticAnimalDetailsData = aquaticAnimal
    }
}