package com.example.foodrecipes.module

import com.example.foodrecipes.api.RecipesApi
import com.example.foodrecipes.repo.RecipesRepo
import com.example.foodrecipes.util.Constant
import com.example.foodrecipes.util.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun getRetrofit() = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun getRecipesApi(retrofit: Retrofit) = retrofit.create(RecipesApi::class.java)



}