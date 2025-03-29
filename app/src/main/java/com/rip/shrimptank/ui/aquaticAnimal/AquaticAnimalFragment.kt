package com.rip.shrimptank.ui.aquaticAnimal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.rip.shrimptank.R
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimal


class AquaticAnimalFragment : Fragment() {

    private val args by navArgs<AquaticAnimalFragmentArgs>()
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_aquatic_animal, container, false)
        loadAquaticAnimalDetails(args.selectedAquaticAnimal)

        return root
    }

    private fun loadAquaticAnimalDetails(aquaticAnimal: AquaticAnimal) {
        root.findViewById<TextView>(R.id.aquaticAnimalName).text = aquaticAnimal.name
        root.findViewById<TextView>(R.id.aquaticAnimalType).text = aquaticAnimal.aquariumType
        root.findViewById<TextView>(R.id.aquaticAnimalDescription).text = aquaticAnimal.info
    }
}