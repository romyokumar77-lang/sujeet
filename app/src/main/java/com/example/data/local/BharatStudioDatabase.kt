package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
  entities = [
    StudioEntity::class,
    BookingEntity::class,
    PaymentOrderEntity::class,
    StudioPayoutEntity::class,
    MembershipPaymentEntity::class
  ],
  version = 1,
  exportSchema = false
)
abstract class BharatStudioDatabase : RoomDatabase() {
  abstract fun studioDao(): BharatStudioDao

  companion object {
    @Volatile
    private var INSTANCE: BharatStudioDatabase? = null

    fun getDatabase(context: Context): BharatStudioDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          BharatStudioDatabase::class.java,
          "bharat_studio_database"
        ).fallbackToDestructiveMigration().build()
        INSTANCE = instance
        instance
      }
    }
  }
}
