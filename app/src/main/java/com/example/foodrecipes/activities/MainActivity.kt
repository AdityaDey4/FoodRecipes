package com.example.foodrecipes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrecipes.adapter.ClickListener
import com.example.foodrecipes.adapter.RandomRecipeAdapter
import com.example.foodrecipes.databinding.ActivityMainBinding
import com.example.foodrecipes.models.Recipes
import com.example.foodrecipes.util.Constant
import com.example.foodrecipes.util.Resource
import com.example.foodrecipes.viewmodels.RandomRecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ClickListener {
    private lateinit var binding: ActivityMainBinding
    lateinit var randomRecipeViewModel: RandomRecipeViewModel
    lateinit var randomRecipeAdapter: RandomRecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        randomRecipeAdapter = RandomRecipeAdapter(this)
        randomRecipeViewModel = ViewModelProvider(this).get(RandomRecipeViewModel::class.java)


        binding.apply {
            randomRecyclerView.adapter = randomRecipeAdapter
            randomRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            randomRecyclerView.addOnScrollListener(myScrollListener)
        }


        randomRecipeViewModel.randomRecipe.observe(this, Observer { resource->
            when(resource) {
                is Resource.Loading -> showProgressBar()
                is Resource.Error -> {
                    hideProgressBar()
                    resource.msg?.let {
                        Snackbar.make(binding.randomRecipeActivity, it, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Refresh"){
                                randomRecipeViewModel.getRandomRecipes()
                            }.show()
                    }
                }
                is Resource.Success -> {
                    hideProgressBar()
                    resource.data?.let {  recipe ->
                        randomRecipeAdapter.differ.submitList(recipe.recipes.toList())
                    }
                }
            }
        })

        binding.searchIV.setOnClickListener {
            startActivity(Intent(this, SearchRecipe::class.java))
        }
    }
    private fun showProgressBar() {
        binding.progressbar.visibility = View.VISIBLE
        isLoading = true
    }
    private fun hideProgressBar() {
        binding.progressbar.visibility = View.INVISIBLE
        isLoading = false
    }

    var isLoading = false
    var isScrolling =  false

    val myScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isAtLastItem = firstVisibleItemPosition+visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= Constant.TOTAL_RECIPES_IN_ONE_PAGE

            val shouldPaginate = !isLoading && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if(shouldPaginate){
                randomRecipeViewModel.getRandomRecipes()
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

    }

    override fun onItemClick(recipe: Recipes) {
        val intent = Intent(this, RecipeDetails::class.java)
        intent.putExtra("recipe", recipe.id)
        startActivity(intent)
    }
}