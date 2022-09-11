package com.example.foodrecipes.models

data class SearchRecipes(
    val number: Int,
    val offset: Int,
    val results: List<SearchRecipesItem>,
    val totalResults: Int
)