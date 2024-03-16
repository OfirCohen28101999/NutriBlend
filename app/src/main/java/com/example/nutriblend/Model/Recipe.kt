package com.example.nutriblend.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nutriblend.base.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot

@Entity
data class Recipe(@PrimaryKey var id: String,
                   var title: String,
                   var ingredients: String,
                   var preparationSteps: String,
                   var imageUrl: String?,
                   var creatingUserId: String,
                   var lastUpdated: Long? = null,
                   var calories: Double? = null,
                   var fat_total_g: Double? = null,
                   var fat_saturated_g: Double? = null,
                   var protein_g: Double? = null,
                   var sodium_mg: Double? = null,
                   var potassium_mg: Double? = null,
                   var cholesterol_mg: Double? = null,
                   var carbohydrates_total_g: Double? = null,
                   var fiber_g: Double? = null,
                   var sugar_g: Double? = null
) {

    companion object {

        var lastUpdated: Long
            get() {
                return MyApplication.Globals.appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.getLong(
                    GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                MyApplication.Globals?.appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()?.putLong(
                    GET_LAST_UPDATED, value)?.apply()
            }

        const val GET_LAST_UPDATED = "get_last_updated"

        // properties
        const val TITLE_KEY = "title"
        const val INGREDIENTS_KEY = "ingredients"
        const val PREPARATION_STEPS_KEY = "preparationSteps"
        const val IMAGE_URL_KEY = "imageUrl"
        const val CREATING_USER_ID_KEY = "creatingUserId"
        const val LAST_UPDATED_KEY = "lastUpdated"
        const val CALORIES_KEY = "calories"
        const val FAT_TOTAL_KEY = "fat_total_g"
        const val FAT_SATURATED_KEY = "fat_saturated_g"
        const val PROTEIN_KEY = "protein_g"
        const val SODIUM_KEY = "sodium_mg"
        const val POTASSIUM_KEY = "potassium_mg"
        const val CHOLESTEROL_KEY = "cholesterol_mg"
        const val CARBOHYDRATES_KEY = "carbohydrates_total_g"
        const val FIBER_KEY = "fiber_g"
        const val SUGAR_KEY = "sugar_g"

        fun fromJson(json: QueryDocumentSnapshot): Recipe {
            val id = json.id
            val title = json[TITLE_KEY] as? String ?: ""
            val ingredients = json[INGREDIENTS_KEY] as? String? ?: ""
            val preparationSteps = json[PREPARATION_STEPS_KEY] as? String? ?: ""
            val imageUrl = json[IMAGE_URL_KEY] as? String
            val creatingUserId = json[CREATING_USER_ID_KEY] as? String? ?: ""
            val lastUpdated: Long? = (json[LAST_UPDATED_KEY] as? Timestamp)?.seconds ?: 0
            val calories = json[CALORIES_KEY] as? Double ?: 0.00
            val fat_total_g = json[FAT_TOTAL_KEY] as? Double ?: 0.00
            val fat_saturated_g = json[FAT_SATURATED_KEY] as? Double ?: 0.00
            val protein_g = json[PROTEIN_KEY] as? Double ?: 0.00
            val sodium_mg = json[SODIUM_KEY] as? Double ?: 0.00
            val potassium_mg = json[POTASSIUM_KEY] as? Double ?: 0.00
            val cholesterol_mg = json[CHOLESTEROL_KEY] as? Double ?: 0.00
            val carbohydrates_total_g = json[CARBOHYDRATES_KEY] as? Double ?: 0.00
            val fiber_g = json[FIBER_KEY] as? Double ?: 0.00
            val sugar_g = json[SUGAR_KEY] as? Double ?: 0.00

            return Recipe(
                id,
                title,
                ingredients,
                preparationSteps,
                imageUrl,
                creatingUserId,
                lastUpdated,
                calories,
                fat_total_g,
                fat_saturated_g,
                protein_g,
                sodium_mg,
                potassium_mg,
                cholesterol_mg,
                carbohydrates_total_g,
                fiber_g,
                sugar_g
            )
        }
    }

    val json: HashMap<String, Any?>
        get() {
            return hashMapOf(
                    TITLE_KEY to title,
                    INGREDIENTS_KEY to ingredients,
                    PREPARATION_STEPS_KEY to preparationSteps,
                    IMAGE_URL_KEY to imageUrl,
                    CREATING_USER_ID_KEY to creatingUserId,
                    LAST_UPDATED_KEY to FieldValue.serverTimestamp(),
                    CALORIES_KEY to calories,
                    FAT_TOTAL_KEY to fat_total_g,
                    FAT_SATURATED_KEY to fat_saturated_g,
                    PROTEIN_KEY to protein_g,
                    SODIUM_KEY to sodium_mg,
                    POTASSIUM_KEY to potassium_mg,
                    CHOLESTEROL_KEY to cholesterol_mg,
                    CARBOHYDRATES_KEY to carbohydrates_total_g,
                    FIBER_KEY to fiber_g,
                    SUGAR_KEY to sugar_g
            )
    }
}
