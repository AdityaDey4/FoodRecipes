package com.example.foodrecipes.viewmodels

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.models.RandomRecipes
import com.example.foodrecipes.repo.RecipesRepo
import com.example.foodrecipes.util.MyApplication
import com.example.foodrecipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class RandomRecipeViewModel @Inject constructor(
    val repo: RecipesRepo,
    val app: MyApplication
) : AndroidViewModel(app) {

    val randomRecipe = MutableLiveData<Resource<RandomRecipes>>()
    var randomRecipeList: RandomRecipes? = null

    init {
        getRandomRecipes()
    }

    fun getRandomRecipes() {
        viewModelScope.launch {
            randomRecipe.postValue(Resource.Loading())
            try {
                val response = repo.getRandomRecipes()
                randomRecipe.postValue(handleRandomRecipeResponse(response))
            } catch (e: Throwable) {
                if (e is IOException) {
                    randomRecipe.postValue(Resource.Error("Turn on Internet"))
                } else {
                    randomRecipe.postValue(Resource.Error("Conversion Problem"))
                }
            }
        }
    }

    private fun handleRandomRecipeResponse(response: Response<RandomRecipes>): Resource<RandomRecipes> {
        if(response.isSuccessful) {
            response.body()?.let { newResponse->
                if(randomRecipeList == null) {
                    randomRecipeList = newResponse
                }else{
                    val oldRecipeList = randomRecipeList?.recipes
                    val newRecipeList = newResponse.recipes

                    oldRecipeList?.addAll(newRecipeList)
                }
                return Resource.Success(randomRecipeList ?: newResponse)
            }
        }
        return Resource.Error(msg = response.message())
    }
}