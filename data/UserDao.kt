package com.example.main.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPlan(plan: WorkoutPlan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNutritionPlan(plan: NutritionPlan)

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT * FROM user_profile WHERE userId = :userId LIMIT 1")
    suspend fun getUserProfile(userId: Int): UserProfile?

    @Query("SELECT * FROM workout_plan WHERE userId = :userId LIMIT 1")
    suspend fun getWorkoutPlan(userId: Int): WorkoutPlan?

    @Query("SELECT * FROM nutrition_plan WHERE userId = :userId LIMIT 1")
    suspend fun getNutritionPlan(userId: Int): NutritionPlan?
}