package com.example.data.model

enum class UserRole {
  CUSTOMER,
  STUDIO_PARTNER,
  ADMIN_MODERATOR
}

data class UserProfile(
  val uid: String = "usr_in_88291",
  val role: UserRole = UserRole.CUSTOMER,
  val name: String = "Aarav Sharma",
  val phone: String = "+91 98765 43210",
  val language: String = "English",
  val city: String = "Jaipur"
)

enum class VerificationStatus {
  VERIFIED,
  PENDING_VERIFICATION,
  UNVERIFIED
}

data class StudioPackage(
  val id: String,
  val title: String,
  val price: Double,
  val inclusions: String,
  val durationHours: String
)

data class PhotographerMember(
  val id: String,
  val name: String,
  val phone: String,
  val gear: String,
  val specialization: String,
  val isAvailable: Boolean = true
)

data class Studio(
  val id: String,
  val ownerUid: String,
  val name: String,
  val city: String,
  val address: String,
  val geoLat: Double,
  val geoLng: Double,
  val services: List<String>,
  val priceRange: String,
  val basePrice: Double,
  val rating: Double,
  val reviewCount: Int,
  val verificationStatus: VerificationStatus,
  val membershipExpiry: String, // e.g. "30 Nov 2026"
  val phone: String,
  val email: String,
  val description: String,
  val packages: List<StudioPackage>,
  val photographers: List<PhotographerMember>
)

enum class PaymentStatus {
  ORDER_CREATED,
  PAYMENT_VERIFIED,
  REFUND_REQUESTED,
  REFUNDED,
  FAILED
}

enum class BookingStatus {
  CONFIRMED,
  PHOTOGRAPHER_ASSIGNED,
  SHOOT_IN_PROGRESS,
  EDITING_PHASE,
  DELIVERED,
  CANCELLED
}

data class Booking(
  val id: String,
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
  val paymentStatus: PaymentStatus,
  val bookingStatus: BookingStatus,
  val assignedPhotographerUid: String? = null,
  val assignedPhotographerName: String? = null,
  val notes: String = "",
  val createdAt: String
)

enum class PayoutStatus {
  INITIATED,
  IN_TRANSIT,
  SETTLED_TO_BANK
}

data class StudioPayout(
  val payoutId: String,
  val studioId: String,
  val studioName: String,
  val bookingId: String,
  val grossAmount: Double,
  val platformFee: Double,
  val netAmount: Double,
  val gatewayPayoutId: String,
  val status: PayoutStatus,
  val bankAccountMasked: String,
  val timestamp: String
)

enum class MembershipStatus {
  ACTIVE,
  EXPIRING_SOON,
  EXPIRED
}

data class MembershipPayment(
  val paymentId: String,
  val studioId: String,
  val amount: Double = 500.0,
  val status: MembershipStatus,
  val expiryDate: String,
  val transactionRef: String,
  val paidAt: String
)

data class PaymentOrder(
  val paymentId: String,
  val gatewayOrderId: String,
  val gatewayPaymentId: String,
  val bookingId: String,
  val amount: Double,
  val status: PaymentStatus,
  val signatureVerified: Boolean,
  val idempotencyKey: String,
  val paymentMethod: String,
  val timestamp: String
)
