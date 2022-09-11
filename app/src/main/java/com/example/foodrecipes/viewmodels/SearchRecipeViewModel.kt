package com.example.foodrecipes.viewmodels

import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodrecipes.models.SearchRecipes
import com.example.foodrecipes.repo.RecipesRepo
import com.example.foodrecipes.util.MyApplication
import com.example.foodrecipes.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchRecipeViewModel @Inject constructor(
    val repo: RecipesRepo,
    val app: MyApplication
): AndroidViewModel(app) {

    var searchRecipes = MutableLiveData<Resource<SearchRecipes>>()
    fun getSearchRecipes(q: String) {
        viewModelScope.launch {
            searchRecipes.postValue(Resource.Loading())
            try {
                val response = repo.getSearchedRecipes(q)
                if(response.isSuccessful) {
                    response.body()?.let {
                        searchRecipes.postValue(Resource.Success(it))
                        Log.d("check",it.totalResults.toString())
                    }
                }
            }catch (e: Throwable) {
                when(e) {
                    is IOException -> searchRecipes.postValue(Resource.Error("Turn On Internet"))
                    else -> searchRecipes.postValue(Resource.Error("Conversion Problem"))
                }
            }
        }
    }
}