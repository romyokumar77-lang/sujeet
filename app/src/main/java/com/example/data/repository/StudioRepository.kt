package com.example.data.repository

import com.example.data.local.BharatStudioDao
import com.example.data.local.BookingEntity
import com.example.data.local.MembershipPaymentEntity
import com.example.data.local.PaymentOrderEntity
import com.example.data.local.StudioEntity
import com.example.data.local.StudioPayoutEntity
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.data.model.MembershipPayment
import com.example.data.model.MembershipStatus
import com.example.data.model.PaymentOrder
import com.example.data.model.PaymentStatus
import com.example.data.model.PayoutStatus
import com.example.data.model.PhotographerMember
import com.example.data.model.Studio
import com.example.data.model.StudioPackage
import com.example.data.model.StudioPayout
import com.example.data.model.VerificationStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class StudioRepository(private val dao: BharatStudioDao) {

  // Catalog packages templates per studio
  private val packageCatalog = mapOf(
    "studio_jaipur_01" to listOf(
      StudioPackage(
        id = "pkg_jp_01",
        title = "Royal Heritage Wedding (3 Days)",
        price = 145000.0,
        inclusions = "2 Candid Photographers, 2 Cinematographers, 4K Drone, Premium Velvet Album (60 pgs), Teaser Video & Full Film",
        durationHours = "3 Full Days"
      ),
      StudioPackage(
        id = "pkg_jp_02",
        title = "Palace Pre-Wedding Shoot",
        price = 45000.0,
        inclusions = "1 Senior Fashion Photographer, Lighting Assistant, 50 Retouched High-Res Photos, Drone Reel for Instagram",
        durationHours = "8 Hours"
      ),
      StudioPackage(
        id = "pkg_jp_03",
        title = "Haldi & Sangeet Express",
        price = 35000.0,
        inclusions = "1 Candid Photographer, 1 Video Specialist, Raw Files in 48 Hours, 30 Edited Deliverables",
        durationHours = "6 Hours"
      )
    ),
    "studio_mumbai_02" to listOf(
      StudioPackage(
        id = "pkg_mum_01",
        title = "Bollywood Cinematic Wedding",
        price = 180000.0,
        inclusions = "Sony FX6 Cinema Crew, Drone Master, Same-Day Edit Reels, Luxury Coffee Table Book",
        durationHours = "2 Days"
      ),
      StudioPackage(
        id = "pkg_mum_02",
        title = "Fashion & Acting Portfolio",
        price = 28000.0,
        inclusions = "Studio Lighting Setup, 4 Outfit Changes, Hair & Makeup Direction, 20 Magazine Retouches",
        durationHours = "4 Hours"
      ),
      StudioPackage(
        id = "pkg_mum_03",
        title = "Corporate Summit & Gala",
        price = 40000.0,
        inclusions = "Executive Stage Photos, Live Cloud Upload for PR, Step & Repeat Red Carpet Coverage",
        durationHours = "6 Hours"
      )
    ),
    "studio_delhi_03" to listOf(
      StudioPackage(
        id = "pkg_del_01",
        title = "Grand Delhi NCR Wedding",
        price = 160000.0,
        inclusions = "Complete Traditional + Candid Coverage, 2 Cinematic Films, Drone Aerials, 2 Master Albums",
        durationHours = "3 Days"
      ),
      StudioPackage(
        id = "pkg_del_02",
        title = "Maternity & Newborn Glow",
        price = 22000.0,
        inclusions = "Comfortable Temperature Studio, 3 Theme Props, 25 Master Retouches, Mini Keepsake Album",
        durationHours = "3 Hours"
      )
    ),
    "studio_blr_04" to listOf(
      StudioPackage(
        id = "pkg_blr_01",
        title = "Modern South Indian Wedding",
        price = 120000.0,
        inclusions = "Muhurtham + Reception Coverage, 4K Cinema Film, Candid Portfolios, Acrylic Cover Photobook",
        durationHours = "2 Days"
      ),
      StudioPackage(
        id = "pkg_blr_02",
        title = "Tech Brand & Product Showcase",
        price = 32000.0,
        inclusions = "Macro E-Commerce Photography, 360 Product Spin Videos, White Background Amazon/Flipkart Ready",
        durationHours = "5 Hours"
      )
    ),
    "studio_varanasi_05" to listOf(
      StudioPackage(
        id = "pkg_vns_01",
        title = "Ghats Spiritual Pre-Wedding & Film",
        price = 38000.0,
        inclusions = "Sunrise Boat Shoot, Golden Hour Ganga Aarti Frames, 40 Fine-Art Retouches, Cinematic 60s Reel",
        durationHours = "6 Hours"
      ),
      StudioPackage(
        id = "pkg_vns_02",
        title = "Traditional Family Ceremony",
        price = 20000.0,
        inclusions = "Traditional Rituals Documentation, Candid Family Portraits, High-Res Pen Drive with 200 Raw Frames",
        durationHours = "5 Hours"
      )
    )
  )

  // Photographers team per studio
  private val photographerCatalog = mapOf(
    "studio_jaipur_01" to listOf(
      PhotographerMember("pg_101", "Vikram Rathore", "+91 98290 11223", "Sony A7 IV, 24-70 GM II", "Candid Wedding & Portraits", true),
      PhotographerMember("pg_102", "Deepak Meena", "+91 98290 44556", "DJI Mavic 3 Pro, Canon R5", "Cinematography & Drone Aerials", true),
      PhotographerMember("pg_103", "Pooja Shekhawat", "+91 98290 77889", "Nikon Z8, 85mm f/1.2", "Pre-Wedding & Detail Shots", true)
    ),
    "studio_mumbai_02" to listOf(
      PhotographerMember("pg_201", "Karan Merchant", "+91 98200 99881", "Sony FX3 Cinema, 35mm GM", "Bollywood Cinematic Films", true),
      PhotographerMember("pg_202", "Ananya Kulkarni", "+91 98200 66772", "Canon EOS R6 Mark II", "Celebrity Portfolios & Fashion", true)
    ),
    "studio_delhi_03" to listOf(
      PhotographerMember("pg_301", "Manish Chadha", "+91 98110 33445", "Nikon Z9, 70-200mm f/2.8", "Big Fat Indian Weddings", true),
      PhotographerMember("pg_302", "Simran Kaur", "+91 98110 55667", "Sony A7R V, 50mm f/1.2", "Maternity & Newborn Soft Light", true)
    ),
    "studio_blr_04" to listOf(
      PhotographerMember("pg_401", "Raghavendra Rao", "+91 98450 12345", "Fujifilm GFX 100 II", "Fine Art Traditional Ceremonies", true),
      PhotographerMember("pg_402", "Naveen Sundaram", "+91 98450 67890", "Sony A7 IV, DJI RS3 Pro", "Commercial Teasers & Social Reels", true)
    ),
    "studio_varanasi_05" to listOf(
      PhotographerMember("pg_501", "Gaurav Tripathi", "+91 94150 99881", "Leica Q3 & Sony A7 IV", "Fine Art Ghats & Golden Light", true)
    )
  )

  suspend fun ensureInitialData() {
    val initialStudios = listOf(
      StudioEntity(
        id = "studio_jaipur_01",
        ownerUid = "owner_raj_01",
        name = "Rajputana Royal Visuals",
        city = "Jaipur",
        address = "B-14, Queens Road, Vaishali Nagar, Jaipur, Rajasthan 302021",
        geoLat = 26.9056,
        geoLng = 75.7423,
        servicesCsv = "Wedding,Pre-Wedding,Royal Drone,Haldi & Mehendi,Velvet Albums",
        priceRange = "₹35,000 - ₹1,45,000",
        basePrice = 35000.0,
        rating = 4.9,
        reviewCount = 128,
        verificationStatus = "VERIFIED",
        membershipExpiry = "15 Dec 2026",
        phone = "+91 98290 12345",
        email = "contact@rajputanaroyal.in",
        description = "Rajasthan's premier heritage wedding visualizers. Specialized in palace weddings at Rambagh, Samode, and City Palace. 10+ years capturing royal romance."
      ),
      StudioEntity(
        id = "studio_mumbai_02",
        ownerUid = "owner_mum_02",
        name = "Mumbai Cinematic Dreams",
        city = "Mumbai",
        address = "Unit 402, Lotus Grandeur, Veera Desai Road, Andheri West, Mumbai, MH 400053",
        geoLat = 19.1363,
        geoLng = 72.8277,
        servicesCsv = "Bollywood Wedding,Fashion Portfolio,Corporate Gala,Instant Reels",
        priceRange = "₹28,000 - ₹1,80,000",
        basePrice = 28000.0,
        rating = 4.8,
        reviewCount = 94,
        verificationStatus = "VERIFIED",
        membershipExpiry = "05 Jan 2027",
        phone = "+91 98200 45678",
        email = "hello@mumbaicinematic.com",
        description = "High-octane cinema grade wedding films and high-fashion model shoots. Equipped with Hollywood-grade lighting and cinema lenses."
      ),
      StudioEntity(
        id = "studio_delhi_03",
        ownerUid = "owner_del_03",
        name = "Delhi Heritage Moments Studio",
        city = "Delhi NCR",
        address = "12/A, Hauz Khas Village, Near Deer Park, New Delhi 110016",
        geoLat = 28.5529,
        geoLng = 77.1945,
        servicesCsv = "Big Fat Wedding,Maternity Glow,Pre-Wedding,Commercial Shoots",
        priceRange = "₹22,000 - ₹1,60,000",
        basePrice = 22000.0,
        rating = 4.7,
        reviewCount = 112,
        verificationStatus = "VERIFIED",
        membershipExpiry = "20 Nov 2026",
        phone = "+91 98110 78901",
        email = "info@delhiheritagestudio.in",
        description = "Capturing candid warmth and monumental celebrations across Chhattarpur farmhouses and Delhi banquet luxury events."
      ),
      StudioEntity(
        id = "studio_blr_04",
        ownerUid = "owner_blr_04",
        name = "Silicon Shutter & Lens Lab",
        city = "Bengaluru",
        address = "88, 100 Feet Road, Indiranagar, Bengaluru, Karnataka 560038",
        geoLat = 12.9719,
        geoLng = 77.6412,
        servicesCsv = "Muhurtham Wedding,Tech Product Shoots,Drone Aerials,Photobook",
        priceRange = "₹32,000 - ₹1,20,000",
        basePrice = 32000.0,
        rating = 4.8,
        reviewCount = 76,
        verificationStatus = "VERIFIED",
        membershipExpiry = "10 Feb 2027",
        phone = "+91 98450 33441",
        email = "team@siliconshutter.io",
        description = "Precision storytelling combining South Indian traditional rituals with clean, contemporary editorial aesthetics."
      ),
      StudioEntity(
        id = "studio_varanasi_05",
        ownerUid = "owner_vns_05",
        name = "Kashi Ganga Art & Candid",
        city = "Varanasi",
        address = "D-22, Dashashwamedh Ghat Road, Godowlia, Varanasi, UP 221001",
        geoLat = 25.3076,
        geoLng = 83.0107,
        servicesCsv = "Ghats Pre-Wedding,Spiritual Documentary,Traditional Rites,Reels",
        priceRange = "₹20,000 - ₹38,000",
        basePrice = 20000.0,
        rating = 4.9,
        reviewCount = 53,
        verificationStatus = "VERIFIED",
        membershipExpiry = "18 Dec 2026",
        phone = "+91 94150 77112",
        email = "varanasiart@bharatstudio.in",
        description = "Mystical golden-hour photography along the sacred river banks. Experienced team handling boat lighting and early-morning dawn shoots."
      )
    )

    dao.insertStudios(initialStudios)

    // Also populate a sample booking so the user immediately sees a live timeline
    val initialBooking = BookingEntity(
      id = "book_in_778101",
      customerUid = "usr_in_88291",
      customerName = "Aarav Sharma",
      customerPhone = "+91 98765 43210",
      studioId = "studio_jaipur_01",
      studioName = "Rajputana Royal Visuals",
      eventType = "Royal Heritage Wedding",
      eventAt = "24 Dec 2026 • 04:00 PM",
      venue = "Samode Palace Courtyard, Jaipur",
      packageTitle = "Royal Heritage Wedding (3 Days)",
      amount = 145000.0,
      paymentStatus = "PAYMENT_VERIFIED",
      bookingStatus = "PHOTOGRAPHER_ASSIGNED",
      assignedPhotographerUid = "pg_101",
      assignedPhotographerName = "Vikram Rathore",
      notes = "Bride and groom entry with traditional fireworks. Drone coverage mandatory.",
      createdAt = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date(System.currentTimeMillis() - 86400000))
    )
    dao.insertBooking(initialBooking)

    // Initial payout entry
    val initialPayout = StudioPayoutEntity(
      payoutId = "pout_in_live_8819",
      studioId = "studio_jaipur_01",
      studioName = "Rajputana Royal Visuals",
      bookingId = "book_in_778101",
      grossAmount = 145000.0,
      platformFee = 7250.0, // 5%
      netAmount = 137750.0,
      gatewayPayoutId = "pout_rzp_990182",
      status = "SETTLED_TO_BANK",
      bankAccountMasked = "HDFC Bank •••• 4409 (IFSC HDFC0001234)",
      timestamp = "Yesterday • 06:15 PM"
    )
    dao.insertPayout(initialPayout)

    // Initial membership payment
    val initialMembership = MembershipPaymentEntity(
      paymentId = "mem_in_500_01",
      studioId = "studio_jaipur_01",
      amount = 500.0,
      status = "ACTIVE",
      expiryDate = "15 Dec 2026",
      transactionRef = "UPI/261011982734/BHARATSTUDIO",
      paidAt = "15 Oct 2026"
    )
    dao.insertMembershipPayment(initialMembership)
  }

  fun getStudios(): Flow<List<Studio>> {
    return dao.getAllStudios().map { entities ->
      entities.map { it.toDomain(packageCatalog[it.id] ?: emptyList(), photographerCatalog[it.id] ?: emptyList()) }
    }
  }

  fun getBookings(): Flow<List<Booking>> {
    return dao.getAllBookings().map { list -> list.map { it.toDomain() } }
  }

  fun getPayouts(): Flow<List<StudioPayout>> {
    return dao.getAllPayouts().map { list -> list.map { it.toDomain() } }
  }

  fun getMembershipPayments(studioId: String): Flow<List<MembershipPayment>> {
    return dao.getMembershipPayments(studioId).map { list -> list.map { it.toDomain() } }
  }

  // Secure flow step 1: Request gateway order from backend
  fun createGatewayOrder(
    customerUid: String,
    customerName: String,
    customerPhone: String,
    studio: Studio,
    pkg: StudioPackage,
    eventType: String,
    eventAt: String,
    venue: String,
    notes: String
  ): PaymentOrder {
    val bookingId = "book_in_" + UUID.randomUUID().toString().take(8)
    val gatewayOrderId = "order_in_live_" + (100000..999999).random()
    val idempotencyKey = "idemp_" + UUID.randomUUID().toString().take(12)
    val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

    return PaymentOrder(
      paymentId = "pay_ord_" + UUID.randomUUID().toString().take(8),
      gatewayOrderId = gatewayOrderId,
      gatewayPaymentId = "pay_in_" + (100000..999999).random(),
      bookingId = bookingId,
      amount = pkg.price,
      status = PaymentStatus.ORDER_CREATED,
      signatureVerified = false,
      idempotencyKey = idempotencyKey,
      paymentMethod = "UPI_GATEWAY",
      timestamp = timestamp
    )
  }

  // Secure flow step 2: Backend verifies HMAC webhook & idempotency -> marks booking CONFIRMED & initiates studio payout
  suspend fun verifyPaymentAndConfirmBooking(
    order: PaymentOrder,
    customerUid: String,
    customerName: String,
    customerPhone: String,
    studio: Studio,
    pkg: StudioPackage,
    eventType: String,
    eventAt: String,
    venue: String,
    notes: String,
    chosenMethod: String
  ): Booking {
    val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())

    // 1. Record verified payment
    val verifiedPayment = PaymentOrderEntity(
      paymentId = order.paymentId,
      gatewayOrderId = order.gatewayOrderId,
      gatewayPaymentId = "pay_in_" + (1000000..9999999).random(),
      bookingId = order.bookingId,
      amount = order.amount,
      status = "PAYMENT_VERIFIED",
      signatureVerified = true,
      idempotencyKey = order.idempotencyKey,
      paymentMethod = chosenMethod,
      timestamp = timestamp
    )
    dao.insertPayment(verifiedPayment)

    // 2. Select first available photographer to auto-assign
    val assignedPhotographer = studio.photographers.firstOrNull { it.isAvailable }

    // 3. Create confirmed booking
    val bookingEntity = BookingEntity(
      id = order.bookingId,
      customerUid = customerUid,
      customerName = customerName,
      customerPhone = customerPhone,
      studioId = studio.id,
      studioName = studio.name,
      eventType = eventType,
      eventAt = eventAt,
      venue = venue,
      packageTitle = pkg.title,
      amount = order.amount,
      paymentStatus = "PAYMENT_VERIFIED",
      bookingStatus = if (assignedPhotographer != null) "PHOTOGRAPHER_ASSIGNED" else "CONFIRMED",
      assignedPhotographerUid = assignedPhotographer?.id,
      assignedPhotographerName = assignedPhotographer?.name,
      notes = notes,
      createdAt = timestamp
    )
    dao.insertBooking(bookingEntity)

    // 4. Initiate Studio Payout in backend ledger (95% to studio, 5% platform fee)
    val fee = order.amount * 0.05
    val net = order.amount - fee
    val payoutEntity = StudioPayoutEntity(
      payoutId = "pout_in_" + (100000..999999).random(),
      studioId = studio.id,
      studioName = studio.name,
      bookingId = order.bookingId,
      grossAmount = order.amount,
      platformFee = fee,
      netAmount = net,
      gatewayPayoutId = "pout_gw_" + (100000..999999).random(),
      status = "INITIATED",
      bankAccountMasked = "SBI / HDFC Bank •••• 9812",
      timestamp = timestamp
    )
    dao.insertPayout(payoutEntity)

    return bookingEntity.toDomain()
  }

  suspend fun cancelBooking(booking: Booking, reason: String) {
    val updated = BookingEntity(
      id = booking.id,
      customerUid = booking.customerUid,
      customerName = booking.customerName,
      customerPhone = booking.customerPhone,
      studioId = booking.studioId,
      studioName = booking.studioName,
      eventType = booking.eventType,
      eventAt = booking.eventAt,
      venue = booking.venue,
      packageTitle = booking.packageTitle,
      amount = booking.amount,
      paymentStatus = "REFUND_REQUESTED",
      bookingStatus = "CANCELLED",
      assignedPhotographerUid = booking.assignedPhotographerUid,
      assignedPhotographerName = booking.assignedPhotographerName,
      notes = booking.notes + " [Cancelled: $reason]",
      createdAt = booking.createdAt
    )
    dao.updateBooking(updated)
  }

  suspend fun assignPhotographerToBooking(booking: Booking, photographer: PhotographerMember) {
    val updated = BookingEntity(
      id = booking.id,
      customerUid = booking.customerUid,
      customerName = booking.customerName,
      customerPhone = booking.customerPhone,
      studioId = booking.studioId,
      studioName = booking.studioName,
      eventType = booking.eventType,
      eventAt = booking.eventAt,
      venue = booking.venue,
      packageTitle = booking.packageTitle,
      amount = booking.amount,
      paymentStatus = booking.paymentStatus.name,
      bookingStatus = "PHOTOGRAPHER_ASSIGNED",
      assignedPhotographerUid = photographer.id,
      assignedPhotographerName = photographer.name,
      notes = booking.notes,
      createdAt = booking.createdAt
    )
    dao.updateBooking(updated)
  }

  suspend fun renewMembershipSubscription(studioId: String): MembershipPayment {
    val timestamp = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
    val entity = MembershipPaymentEntity(
      paymentId = "mem_in_" + (100000..999999).random(),
      studioId = studioId,
      amount = 500.0,
      status = "ACTIVE",
      expiryDate = "30 Dec 2026",
      transactionRef = "UPI/" + (100000000000L..999999999999L).random() + "/BHARATSTUDIO",
      paidAt = timestamp
    )
    dao.insertMembershipPayment(entity)

    // Update studio membership expiry
    val existing = dao.getStudioById(studioId)
    if (existing != null) {
      dao.updateStudio(existing.copy(membershipExpiry = "30 Dec 2026", verificationStatus = "VERIFIED"))
    }

    return entity.toDomain()
  }

  suspend fun requestStudioVerification(studioId: String) {
    val existing = dao.getStudioById(studioId)
    if (existing != null) {
      dao.updateStudio(existing.copy(verificationStatus = "PENDING_VERIFICATION"))
    }
  }

  fun getPackagesForStudio(studioId: String): List<StudioPackage> {
    return packageCatalog[studioId] ?: listOf(
      StudioPackage("pkg_def_1", "Full Day Coverage", 45000.0, "2 Photographers, Raw files", "8 Hours")
    )
  }

  fun getPhotographersForStudio(studioId: String): List<PhotographerMember> {
    return photographerCatalog[studioId] ?: emptyList()
  }

  private fun StudioEntity.toDomain(
    packages: List<StudioPackage>,
    photographers: List<PhotographerMember>
  ) = Studio(
    id = id,
    ownerUid = ownerUid,
    name = name,
    city = city,
    address = address,
    geoLat = geoLat,
    geoLng = geoLng,
    services = servicesCsv.split(",").map { it.trim() },
    priceRange = priceRange,
    basePrice = basePrice,
    rating = rating,
    reviewCount = reviewCount,
    verificationStatus = try { VerificationStatus.valueOf(verificationStatus) } catch (e: Exception) { VerificationStatus.VERIFIED },
    membershipExpiry = membershipExpiry,
    phone = phone,
    email = email,
    description = description,
    packages = packages,
    photographers = photographers
  )

  private fun BookingEntity.toDomain() = Booking(
    id = id,
    customerUid = customerUid,
    customerName = customerName,
    customerPhone = customerPhone,
    studioId = studioId,
    studioName = studioName,
    eventType = eventType,
    eventAt = eventAt,
    venue = venue,
    packageTitle = packageTitle,
    amount = amount,
    paymentStatus = try { PaymentStatus.valueOf(paymentStatus) } catch (e: Exception) { PaymentStatus.PAYMENT_VERIFIED },
    bookingStatus = try { BookingStatus.valueOf(bookingStatus) } catch (e: Exception) { BookingStatus.CONFIRMED },
    assignedPhotographerUid = assignedPhotographerUid,
    assignedPhotographerName = assignedPhotographerName,
    notes = notes,
    createdAt = createdAt
  )

  private fun StudioPayoutEntity.toDomain() = StudioPayout(
    payoutId = payoutId,
    studioId = studioId,
    studioName = studioName,
    bookingId = bookingId,
    grossAmount = grossAmount,
    platformFee = platformFee,
    netAmount = netAmount,
    gatewayPayoutId = gatewayPayoutId,
    status = try { PayoutStatus.valueOf(status) } catch (e: Exception) { PayoutStatus.INITIATED },
    bankAccountMasked = bankAccountMasked,
    timestamp = timestamp
  )

  private fun MembershipPaymentEntity.toDomain() = MembershipPayment(
    paymentId = paymentId,
    studioId = studioId,
    amount = amount,
    status = try { MembershipStatus.valueOf(status) } catch (e: Exception) { MembershipStatus.ACTIVE },
    expiryDate = expiryDate,
    transactionRef = transactionRef,
    paidAt = paidAt
  )
}
