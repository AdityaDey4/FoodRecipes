package com.example.foodrecipes.models

import android.os.Parcelable
import java.io.Serializable

data class Recipes(
    val aggregateLikes: Int,
    val analyzedInstructions: List<AnalyzedInstruction>,
    val extendedIngredients: List<ExtendedIngredient>,
    val image: String,
    val id: Int,
    val pricePerServing: Double,
    val readyInMinutes: Int,
    val servings: Int,
    val title: String,
) : Serializable