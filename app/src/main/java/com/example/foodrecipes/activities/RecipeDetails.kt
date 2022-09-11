package com.example.foodrecipes.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodrecipes.R
import com.example.foodrecipes.adapter.SimilarRecipeAdapter
import com.example.foodrecipes.adapter.SimilarRecipeClickListener
import com.example.foodrecipes.databinding.ActivityRecipeDetailsBinding
import com.example.foodrecipes.models.Recipes
import com.example.foodrecipes.util.Resource
import com.example.foodrecipes.viewmodels.RecipeDetailsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class RecipeDetails : AppCompatActivity(), SimilarRecipeClickListener {
    lateinit var binding: ActivityRecipeDetailsBinding
    private var recipeId by Delegates.notNull<Int>()
    lateinit var recipeDetailsViewModel: RecipeDetailsViewModel
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        recipeId = intent.getIntExtra("recipe",644488)
        recipeDetailsViewModel = ViewModelProvider(this).get(RecipeDetailsViewModel::class.java)

        recipeDetailsViewModel.setRecipe(recipeId)

        recipeDetailsViewModel.myRecipe.observe(this, Observer { resource->

            when(resource) {
                is Resource.Loading->showProgressBar()
                is Resource.Error-> {
                    binding.progressbarPB.visibility = View.INVISIBLE
                    resource.msg?.let {
                        Snackbar.make(binding.recipeDetailsActivity, it, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Refresh"){
                                recipeDetailsViewModel.setRecipe(recipeId)
                            }.show()
                    }
                }
                is Resource.Success -> {
                    val recipe = resource.data!!
                    recipeDetailsViewModel.getSimilarRecipes(recipe.id)

                    binding.recipeNameTV.text = recipe.title
                    binding.servingsTV.text = "${recipe.servings} Servings"
                    binding.likesTV.text = "${recipe.aggregateLikes} Likes"
                    binding.minutesTV.text = "${recipe.readyInMinutes} Minutes"

                    var instruction = ""
                    val instructionList = recipe.analyzedInstructions[0].steps
                    for(i in instructionList.indices) {
                        instruction += if(i == instructionList.size-1){
                            "${instructionList[i].number}. ${instructionList[i].step}"
                        }else "${instructionList[i].number}. ${instructionList[i].step}\n\n"
                    }

                    binding.instructionTV.text = instruction


                    var ingredient = ""
                    val ingredientList = recipe.extendedIngredients
                    for(i in ingredientList.indices) {
                        ingredient += if(i == instructionList.size-1){
                            "${i+1}. ${ingredientList[i].original}"
                        }else "${i+1}. ${ingredientList[i].original}\n"
                    }

                    binding.ingredientsTV.text = ingredient

                    Glide.with(applicationContext)
                        .load(recipe.image)
                        .error(R.drawable.ic_baseline_cancel_24)
                        .into(binding.recipeImageIV)
                }
            }
        })

        recipeDetailsViewModel.similarRecipesDetails.observe(this, Observer { resource->
            when(resource) {
                is Resource.Loading->showProgressBar()
                is Resource.Success-> {
                    resource.data?.let {list->
                        if(list.isNotEmpty()) {
                            val adapter = SimilarRecipeAdapter(list.toList(), this)
                            binding.similarItemsRV.adapter = adapter
                            //need to add horizontal layout manager
                            val horizontalLayout = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                            binding.similarItemsRV.layoutManager = horizontalLayout
                        }
                        hideProgressBar()
                    }
                }
                is Resource.Error-> {
                    hideProgressBar()
                    resource.msg?.let {
                        Snackbar.make(binding.recipeDetailsActivity, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        })

    }
    private fun hideProgressBar() {
        binding.progressbarPB.visibility = View.INVISIBLE
        binding.mainLinearLayout.visibility = View.VISIBLE
    }
    private fun showProgressBar() {
        binding.progressbarPB.visibility = View.VISIBLE
        binding.mainLinearLayout.visibility = View.INVISIBLE
    }

    override fun onItemClick(recipe: Recipes) {
        val intent = Intent(this, RecipeDetails::class.java)
        intent.putExtra("recipe", recipe.id)
        startActivity(intent)
    }
}