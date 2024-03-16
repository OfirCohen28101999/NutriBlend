package com.example.nutriblend.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nutriblend.Model.User

@Dao
interface UserDao {
    @Query("SELECT * FROM USER")
    fun getAll(): LiveData<MutableList<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM USER WHERE id=:id")
    fun getUserById(id: String): LiveData<User?>
}