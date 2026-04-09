package com.example.main.data
import android.content.Context
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.room.Database

@Database(
    entities = [User::class, UserProfile::class, WorkoutPlan::class, NutritionPlan::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao

    companion object{
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            return instance ?: synchronized(this){
                val instanceBuilder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitbot_database"
                ).fallbackToDestructiveMigration().build()
                instance = instanceBuilder
                instanceBuilder
            }
        }
    }
}