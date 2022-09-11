package com.example.foodrecipes.repo

import com.example.foodrecipes.api.RecipesApi
import com.example.foodrecipes.models.RandomRecipes
import retrofit2.Response
import javax.inject.Inject

class RecipesRepo @Inject constructor(val api : RecipesApi) {

    suspend fun getRandomRecipes()  = api.getRandomRecipes()
    suspend fun getSimilarRecipes(id : Int) = api.getSimilarRecipes(id)
    suspend fun getRecipeInformation(id: Int) = api.getRecipeInformation(id)
    suspend fun getSearchedRecipes(q: String) = api.getSearchedRecipes(q)
}