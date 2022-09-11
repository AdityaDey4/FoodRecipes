package com.example.foodrecipes.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.R
import com.bumptech.glide.Glide
import com.example.foodrecipes.databinding.RecipeTemplateBinding
import com.example.foodrecipes.models.Recipes
import javax.inject.Inject

class RandomRecipeAdapter(
    val clickListener: ClickListener
): RecyclerView.Adapter<RandomRecipeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RecipeTemplateBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RecipeTemplateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            differ.currentList[position]?.let { recipe ->
                binding.tvRecipeNameTemplate.text = recipe.title
                binding.tvLikesTemplates.text = "${recipe.aggregateLikes} likes"
                binding.tvMinutesTemplates.text = "${recipe.readyInMinutes} Minutes"
                binding.tvServingsTemplate.text = "${recipe.servings} Servings"

                Glide.with(binding.root.context)
                    .load(recipe.image)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_cancel_24)
                    .into(binding.ivRecipeImageTemplate)
            }
            itemView.setOnClickListener {
                clickListener.onItemClick(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallBack = object : DiffUtil.ItemCallback<Recipes>() {
        override fun areItemsTheSame(oldItem: Recipes, newItem: Recipes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Recipes, newItem: Recipes): Boolean {
            return oldItem==newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

}
interface ClickListener {
    fun onItemClick(recipe: Recipes)
}