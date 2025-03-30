package com.rip.shrimptank.ui.search

import android.view.View
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimal

class AquaticAnimalSearchResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val aquaticAnimalNameView: TextView?

    init {
        aquaticAnimalNameView = itemView.findViewById(R.id.aquaticAnimalResultName)
    }

    fun bind(aquaticAnimal: AquaticAnimal?) {
        if (aquaticAnimal == null) {
            return
        }
        itemView.setOnClickListener {
            val action = SearchDirections.actionSearchToAquaticAnimalFragment(aquaticAnimal)
            Navigation.findNavController(itemView).navigate(action)
        }

        aquaticAnimalNameView?.text = aquaticAnimal.name
    }
}