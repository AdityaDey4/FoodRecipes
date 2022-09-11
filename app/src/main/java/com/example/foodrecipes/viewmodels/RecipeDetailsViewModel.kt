package com.example.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.api.RecipesApi
import com.example.foodrecipes.models.Recipes
import com.example.foodrecipes.models.SimilarRecipes
import com.example.foodrecipes.models.SimilarRecipesItem
import com.example.foodrecipes.repo.RecipesRepo
import com.example.foodrecipes.util.MyApplication
import com.example.foodrecipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor(
    val repo : RecipesRepo,
    val app : MyApplication
) : AndroidViewModel(app){

    var myRecipe = MutableLiveData<Resource<Recipes>>()
    var similarRecipesDetails = MutableLiveData<Resource<MutableList<Recipes>>>()

    fun setRecipe(recipeId: Int) {
        viewModelScope.launch {
            myRecipe.postValue(Resource.Loading())
            try {
                val response = repo.getRecipeInformation(recipeId)
                if(response.isSuccessful) {
                    response.body()?.let {
                        myRecipe.postValue(Resource.Success(it))
                    }
                }
            } catch (e: Throwable) {
                if (e is IOException) {
                    myRecipe.postValue(Resource.Error("Turn on Internet"))
                } else {
                    myRecipe.postValue(Resource.Error("Conversion Problem"))
                }
            }
        }
    }

    fun getSimilarRecipes(id :Int) {
        viewModelScope.launch {
            similarRecipesDetails.postValue(Resource.Loading())
            try{
                val similarRecipeList = mutableListOf<Recipes>()
                async {
                    val response = repo.getSimilarRecipes(id)
                    if (response.isSuccessful) {
                        response.body()?.let {
                            fetchRecipeInformation(it, similarRecipeList)
                        }
                    }
                }.await()
                similarRecipesDetails.postValue(Resource.Success(similarRecipeList))
            }catch (e: Exception) {
                similarRecipesDetails.postValue(Resource.Error("can't reload similar recipes"))
            }
        }
    }

    private suspend fun fetchRecipeInformation(similarRecipes: SimilarRecipes, similarRecipeList: MutableList<Recipes>) {
        try {
            for(similarRecipe in similarRecipes) {
                val response = repo.getRecipeInformation(similarRecipe.id)
                if(response.isSuccessful) {
                    response.body()?.let {
                        similarRecipeList.add(it)
                    }
                }
            }
        }catch (e: Exception){
            Log.d("check", e.message.toString())
        }
    }

}