package com.example.nutriblend.Model

class Model private constructor(){
    val recipes: MutableList<Recipe> = ArrayList()

    companion object {
        val instance: Model = Model()
    }

    init {
        for (i in 0..20){
            val recipe = Recipe("new recipe title $i",
                "200 grams blueberries, 1 cup flour, half a cup sugar, 1 egg, 1 spoon butter, 1 cup buttermilk",
                "mix in bowl and throw in the oven 180deg for half an hour",
                "id: $i",
                "imageUrl",
                "ofirCohenUserId" )

            recipes.add(recipe)

        }
    }

}