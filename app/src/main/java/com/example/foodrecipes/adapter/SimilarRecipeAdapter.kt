package com.example.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.SimilarRecipeTemplateBinding
import com.example.foodrecipes.models.Recipes

class SimilarRecipeAdapter(
    val recipeList: List<Recipes>,
    val similarRecipeClickListener: SimilarRecipeClickListener
) : RecyclerView.Adapter<SimilarRecipeAdapter.ViewHolder>(){
    inner class ViewHolder(val binding: SimilarRecipeTemplateBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SimilarRecipeTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.similarRecipeNameTV.text = recipeList[position].title
            Glide.with(binding.root.context)
                .load(recipeList[position].image)
                .centerCrop()
                .error(R.drawable.ic_baseline_cancel_24)
                .into(binding.similarRecipeImageIV)
            itemView.setOnClickListener {
                similarRecipeClickListener.onItemClick(recipeList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}
interface SimilarRecipeClickListener {
    fun onItemClick(recipe: Recipes)
}