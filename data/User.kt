package com.example.main.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String
)

@Entity(
    tableName = "user_profile",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"],
        unique = true)]
    )
data class UserProfile(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val age: Int,
    val gender: String,
    val height: Int,
    val weight: Float,
    val goalWeight: Float,
    val goalPhysique: String,
    val gymAccess: String,
    val activityLevel: String,
    val schedule: String,
    val diet: String,
    val injuries: String
)

@Entity(
    tableName = "workout_plan",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"],
        unique = true)]
    )
data class WorkoutPlan(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val content: String
)

@Entity(
    tableName = "nutrition_plan",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"],
        unique = true)]
    )
data class NutritionPlan(
    @PrimaryKey (autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val content: String
)