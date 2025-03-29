package com.rip.shrimptank.ui.aquaticAnimal

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.rip.shrimptank.R


class AquaticAnimalFragment : Fragment() {

    private val args by navArgs<AquaticAnimalFragmentArgs>()
    private lateinit var viewModel: AquaticAnimalViewModel
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        root = inflater.inflate(R.layout.fragment_aquatic_animal, container, false)

        viewModel = ViewModelProvider(this)[AquaticAnimalViewModel::class.java]
        viewModel.setAquaticAnimalDetails(args.selectedAquaticAnimal)
        loadAquaticAnimalDetails(root)

        return root
    }

    private fun loadAquaticAnimalDetails(root: View) {
        val aquaticAnimalName: TextView = root.findViewById(R.id.aquaticAnimalName)
        val aquaticAnimalDescription: TextView = root.findViewById(R.id.aquaticAnimalDescription)

        viewModel.aquaticAnimalDetailsData?.let { aquaticAnimal ->
            aquaticAnimalName.text = aquaticAnimal.name

            aquaticAnimalDescription.text = aquaticAnimal.info
            aquaticAnimalDescription.movementMethod = ScrollingMovementMethod()
        }
    }
}