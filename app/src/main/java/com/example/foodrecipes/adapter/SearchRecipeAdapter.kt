package com.example.foodrecipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.databinding.SearchRecipeTemplateBinding
import com.example.foodrecipes.models.SearchRecipesItem

class SearchRecipeAdapter(
    val searchRecipesItem: List<SearchRecipesItem>,
    val clickListener: SearchRecipeClickListener
): RecyclerView.Adapter<SearchRecipeAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: SearchRecipeTemplateBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SearchRecipeTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            binding.searchRecipeNameTV.text = searchRecipesItem[position].title
            Glide.with(binding.root.context)
                .load(searchRecipesItem[position].image)
                .error(R.drawable.ic_baseline_cancel_24)
                .into(binding.searchRecipeImageIV)

            itemView.setOnClickListener {
                clickListener.onItemClick(searchRecipesItem[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return searchRecipesItem.size
    }
}
interface SearchRecipeClickListener {
    fun onItemClick(id: Int)
}