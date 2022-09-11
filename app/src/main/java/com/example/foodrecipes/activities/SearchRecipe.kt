package com.example.foodrecipes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodrecipes.R
import com.example.foodrecipes.adapter.SearchRecipeAdapter
import com.example.foodrecipes.adapter.SearchRecipeClickListener
import com.example.foodrecipes.databinding.ActivitySearchRecipeBinding
import com.example.foodrecipes.util.Resource
import com.example.foodrecipes.viewmodels.SearchRecipeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchRecipe : AppCompatActivity(), SearchRecipeClickListener {
    lateinit var binding: ActivitySearchRecipeBinding
    lateinit var searchRecipeViewModel: SearchRecipeViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        searchRecipeViewModel = ViewModelProvider(this).get(SearchRecipeViewModel::class.java)
        binding.searchET.setOnEditorActionListener(object: TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchRecipeViewModel.getSearchRecipes(v.toString())
                    binding.searchET.clearFocus()
                    return true
                }
                return false
            }
        })

        searchRecipeViewModel.searchRecipes.observe(this, Observer { resourse->
            when(resourse) {
                is Resource.Loading -> showProgressBar()
                is Resource.Error -> {
                    hideProgressBar()
                    Snackbar.make(binding.searchRecipe, resourse.msg!!, Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Success -> {
                    hideProgressBar()
                    val searchRecipe = resourse.data!!
                    if(searchRecipe.totalResults == 0) {
                        binding.noRecipeFoundTV.visibility = View.VISIBLE
                    }else{
                        val adapter = SearchRecipeAdapter(searchRecipe.results, this)
                        binding.searchRecipeRV.adapter = adapter
                        binding.searchRecipeRV.layoutManager = LinearLayoutManager(this)
                    }
                }
            }
        })
    }
    fun showProgressBar() {
        binding.searchRecipePB.visibility = View.VISIBLE
        binding.noRecipeFoundTV.visibility = View.INVISIBLE
    }
    fun hideProgressBar() {
        binding.searchRecipePB.visibility = View.INVISIBLE
        binding.noRecipeFoundTV.visibility = View.INVISIBLE
    }

    override fun onItemClick(id: Int) {
        val intent = Intent(this, RecipeDetails::class.java)
        intent.putExtra("recipe", id)
        startActivity(intent)
    }
}