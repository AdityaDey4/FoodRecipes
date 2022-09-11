package com.example.foodrecipes.models

import java.io.Serializable

data class AnalyzedInstruction(
    val steps: List<Step>
): Serializable