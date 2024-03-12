package com.example.nutriblend.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.QueryDocumentSnapshot
@Entity
data class User(@PrimaryKey var id: String,
                   var username: String,
                   var firstName: String,
                   var lastName: String,
                   var email: String,
                   var avatarUrl: String?,
                   var lastUpdated: Long? = null,
                   var createdAt: Long? = null
) {

    companion object {
        const val USERNAME_KEY = "username"
        const val FIRST_NAME_KEY = "firstName"
        const val LAST_NAME_KEY = "lastName"
        const val EMAIL_KEY = "email"
        const val AVATAR_URL_KEY = "avatarUrl"
        const val LAST_UPDATED_KEY = "lastUpdated"
        const val CREATED_AT_KEY = "createdAt"

        fun fromJson(json: QueryDocumentSnapshot): User {
            val id = json.id
            val username = json[USERNAME_KEY] as? String ?: ""
            val firstName = json[FIRST_NAME_KEY] as? String ?: ""
            val lastName = json[LAST_NAME_KEY] as? String ?: ""
            val email = json[EMAIL_KEY] as? String ?: ""
            val avatarUrl = json[AVATAR_URL_KEY] as? String?
            val lastUpdated: Long? = (json[LAST_UPDATED_KEY] as? Timestamp)?.seconds ?: 0
            val createdAt: Long? = (json[CREATED_AT_KEY] as? Timestamp)?.seconds ?: 0
            return User(id, username, firstName, lastName, email, avatarUrl, lastUpdated, createdAt)
        }
    }

    val json: HashMap<String, Any?>
        get() {
            return hashMapOf(
                USERNAME_KEY to username,
                FIRST_NAME_KEY to firstName,
                LAST_NAME_KEY to lastName,
                EMAIL_KEY to email,
                AVATAR_URL_KEY to avatarUrl,
                LAST_UPDATED_KEY to FieldValue.serverTimestamp(),
                CREATED_AT_KEY to createdAt,
                )
        }
}
