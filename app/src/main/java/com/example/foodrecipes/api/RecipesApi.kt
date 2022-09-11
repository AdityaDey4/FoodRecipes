package com.example.foodrecipes.api

import com.example.foodrecipes.models.*
import com.example.foodrecipes.util.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipesApi {

    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("apiKey") api: String = Constant.API_KEY,
        @Query("number") number: Int = Constant.TOTAL_RECIPES_IN_ONE_PAGE
    ): Response<RandomRecipes>

    @GET("recipes/{id}/similar")
    suspend fun getSimilarRecipes(
        @Path("id") id: Int,
        @Query("apiKey") api: String = Constant.API_KEY
    ): Response<SimilarRecipes>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("apiKey") api: String = Constant.API_KEY
    ) : Response<Recipes>

    @GET("recipes/complexSearch")
    suspend fun getSearchedRecipes(
        @Query("query") q: String,
        @Query("number") number: Int = Constant.TOTAL_RECIPES_IN_ONE_PAGE,
        @Query("apiKey") api: String = Constant.API_KEY
    ): Response<SearchRecipes>
}