package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "studios")
data class StudioEntity(
  @PrimaryKey val id: String,
  val ownerUid: String,
  val name: String,
  val city: String,
  val address: String,
  val geoLat: Double,
  val geoLng: Double,
  val servicesCsv: String,
  val priceRange: String,
  val basePrice: Double,
  val rating: Double,
  val reviewCount: Int,
  val verificationStatus: String,
  val membershipExpiry: String,
  val phone: String,
  val email: String,
  val description: String
)

@Entity(tableName = "bookings")
data class BookingEntity(
  @PrimaryKey val id: String,
  val customerUid: String,
  val customerName: String,
  val customerPhone: String,
  val studioId: String,
  val studioName: String,
  val eventType: String,
  val eventAt: String,
  val venue: String,
  val packageTitle: String,
  val amount: Double,
  val paymentStatus: String,
  val bookingStatus: String,
  val assignedPhotographerUid: String?,
  val assignedPhotographerName: String?,
  val notes: String,
  val createdAt: String
)

@Entity(tableName = "payments")
data class PaymentOrderEntity(
  @PrimaryKey val paymentId: String,
  val gatewayOrderId: String,
  val gatewayPaymentId: String,
  val bookingId: String,
  val amount: Double,
  val status: String,
  val signatureVerified: Boolean,
  val idempotencyKey: String,
  val paymentMethod: String,
  val timestamp: String
)

@Entity(tableName = "payouts")
data class StudioPayoutEntity(
  @PrimaryKey val payoutId: String,
  val studioId: String,
  val studioName: String,
  val bookingId: String,
  val grossAmount: Double,
  val platformFee: Double,
  val netAmount: Double,
  val gatewayPayoutId: String,
  val status: String,
  val bankAccountMasked: String,
  val timestamp: String
)

@Entity(tableName = "membership_payments")
data class MembershipPaymentEntity(
  @PrimaryKey val paymentId: String,
  val studioId: String,
  val amount: Double,
  val status: String,
  val expiryDate: String,
  val transactionRef: String,
  val paidAt: String
)
