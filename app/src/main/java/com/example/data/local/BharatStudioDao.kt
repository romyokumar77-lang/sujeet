package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BharatStudioDao {

  @Query("SELECT * FROM studios")
  fun getAllStudios(): Flow<List<StudioEntity>>

  @Query("SELECT * FROM studios WHERE id = :studioId LIMIT 1")
  suspend fun getStudioById(studioId: String): StudioEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStudios(studios: List<StudioEntity>)

  @Update
  suspend fun updateStudio(studio: StudioEntity)

  @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
  fun getAllBookings(): Flow<List<BookingEntity>>

  @Query("SELECT * FROM bookings WHERE customerUid = :uid ORDER BY createdAt DESC")
  fun getBookingsForCustomer(uid: String): Flow<List<BookingEntity>>

  @Query("SELECT * FROM bookings WHERE studioId = :studioId ORDER BY createdAt DESC")
  fun getBookingsForStudio(studioId: String): Flow<List<BookingEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertBooking(booking: BookingEntity)

  @Update
  suspend fun updateBooking(booking: BookingEntity)

  @Query("SELECT * FROM payments ORDER BY timestamp DESC")
  fun getAllPayments(): Flow<List<PaymentOrderEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayment(payment: PaymentOrderEntity)

  @Query("SELECT * FROM payouts ORDER BY timestamp DESC")
  fun getAllPayouts(): Flow<List<StudioPayoutEntity>>

  @Query("SELECT * FROM payouts WHERE studioId = :studioId ORDER BY timestamp DESC")
  fun getPayoutsForStudio(studioId: String): Flow<List<StudioPayoutEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertPayout(payout: StudioPayoutEntity)

  @Query("SELECT * FROM membership_payments WHERE studioId = :studioId ORDER BY paidAt DESC")
  fun getMembershipPayments(studioId: String): Flow<List<MembershipPaymentEntity>>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertMembershipPayment(payment: MembershipPaymentEntity)
}
