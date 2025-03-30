package com.rip.shrimptank.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rip.shrimptank.R
import com.rip.shrimptank.model.aquaticAnimal.AquaticAnimal

class SearchRecycleAdapter(var aquaticAnimals: MutableList<AquaticAnimal>?) :
    RecyclerView.Adapter<AquaticAnimalSearchResultViewHolder>() {

    override fun getItemCount(): Int {
        return aquaticAnimals?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AquaticAnimalSearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.aquatic_animal_search_result, parent, false)
        return AquaticAnimalSearchResultViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AquaticAnimalSearchResultViewHolder, position: Int) {
        val aquaticAnimal = aquaticAnimals?.get(position)
        holder.bind(aquaticAnimal)
    }
}