package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.BharatStudioDatabase
import com.example.data.model.Booking
import com.example.data.model.MembershipPayment
import com.example.data.model.PaymentOrder
import com.example.data.model.PhotographerMember
import com.example.data.model.Studio
import com.example.data.model.StudioPackage
import com.example.data.model.StudioPayout
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.data.repository.StudioRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
  EXPLORE,
  STUDIO_DETAIL,
  MY_BOOKINGS,
  PARTNER_PORTAL,
  COMPLIANCE_AND_LAUNCH
}

sealed class BookingCheckoutStep {
  object Idle : BookingCheckoutStep()
  data class FormInput(val studio: Studio, val selectedPackage: StudioPackage) : BookingCheckoutStep()
  data class CreatingOrder(val message: String) : BookingCheckoutStep()
  data class GatewayCheckout(val order: PaymentOrder, val studio: Studio, val pkg: StudioPackage, val venue: String, val eventAt: String, val eventType: String, val notes: String) : BookingCheckoutStep()
  data class VerifyingSignature(val message: String) : BookingCheckoutStep()
  data class BookingSuccess(val booking: Booking, val order: PaymentOrder) : BookingCheckoutStep()
  data class Failure(val error: String) : BookingCheckoutStep()
}

class StudioViewModel(application: Application) : AndroidViewModel(application) {

  private val repository: StudioRepository

  private val _userProfile = MutableStateFlow(UserProfile())
  val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

  private val _currentScreen = MutableStateFlow(AppScreen.EXPLORE)
  val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

  private val _selectedStudio = MutableStateFlow<Studio?>(null)
  val selectedStudio: StateFlow<Studio?> = _selectedStudio.asStateFlow()

  private val _searchQuery = MutableStateFlow("")
  val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

  private val _selectedCity = MutableStateFlow("All Cities")
  val selectedCity: StateFlow<String> = _selectedCity.asStateFlow()

  private val _selectedCategory = MutableStateFlow("All")
  val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

  private val _checkoutStep = MutableStateFlow<BookingCheckoutStep>(BookingCheckoutStep.Idle)
  val checkoutStep: StateFlow<BookingCheckoutStep> = _checkoutStep.asStateFlow()

  private val _statusMessage = MutableStateFlow<String?>(null)
  val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

  val studios: StateFlow<List<Studio>>
  val bookings: StateFlow<List<Booking>>
  val payouts: StateFlow<List<StudioPayout>>

  val filteredStudios: StateFlow<List<Studio>>

  init {
    val database = BharatStudioDatabase.getDatabase(application)
    repository = StudioRepository(database.studioDao())

    viewModelScope.launch {
      repository.ensureInitialData()
    }

    studios = repository.getStudios()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    bookings = repository.getBookings()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    payouts = repository.getPayouts()
      .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    filteredStudios = combine(studios, searchQuery, selectedCity, selectedCategory) { list, query, city, category ->
      list.filter { studio ->
        val matchesQuery = query.isBlank() || studio.name.contains(query, ignoreCase = true) ||
            studio.city.contains(query, ignoreCase = true) ||
            studio.services.any { it.contains(query, ignoreCase = true) }
        val matchesCity = city == "All Cities" || studio.city.equals(city, ignoreCase = true)
        val matchesCategory = category == "All" || studio.services.any { it.contains(category, ignoreCase = true) }
        matchesQuery && matchesCity && matchesCategory
      }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
  }

  fun navigateTo(screen: AppScreen) {
    _currentScreen.value = screen
  }

  fun openStudioDetail(studio: Studio) {
    _selectedStudio.value = studio
    _currentScreen.value = AppScreen.STUDIO_DETAIL
  }

  fun setSearchQuery(query: String) {
    _searchQuery.value = query
  }

  fun setSelectedCity(city: String) {
    _selectedCity.value = city
  }

  fun setSelectedCategory(cat: String) {
    _selectedCategory.value = cat
  }

  fun setUserRole(role: UserRole) {
    _userProfile.value = _userProfile.value.copy(role = role)
  }

  fun setLanguage(lang: String) {
    _userProfile.value = _userProfile.value.copy(language = lang)
  }

  fun clearStatusMessage() {
    _statusMessage.value = null
  }

  fun startBookingFlow(studio: Studio, pkg: StudioPackage) {
    _checkoutStep.value = BookingCheckoutStep.FormInput(studio, pkg)
  }

  fun dismissBookingFlow() {
    _checkoutStep.value = BookingCheckoutStep.Idle
  }

  // Step 2 & 3: Request backend order, open gateway sheet
  fun requestBackendOrderAndOpenGateway(
    studio: Studio,
    pkg: StudioPackage,
    eventType: String,
    eventAt: String,
    venue: String,
    notes: String
  ) {
    viewModelScope.launch {
      _checkoutStep.value = BookingCheckoutStep.CreatingOrder("Requesting booking order from secure backend...")
      delay(750) // simulate network handshake

      val order = repository.createGatewayOrder(
        customerUid = _userProfile.value.uid,
        customerName = _userProfile.value.name,
        customerPhone = _userProfile.value.phone,
        studio = studio,
        pkg = pkg,
        eventType = eventType,
        eventAt = eventAt,
        venue = venue,
        notes = notes
      )

      _checkoutStep.value = BookingCheckoutStep.GatewayCheckout(
        order = order,
        studio = studio,
        pkg = pkg,
        venue = venue,
        eventAt = eventAt,
        eventType = eventType,
        notes = notes
      )
    }
  }

  // Step 4 & 5: Process simulated gateway payment & verify webhook
  fun confirmGatewayPayment(
    gatewayCheckout: BookingCheckoutStep.GatewayCheckout,
    paymentMethod: String,
    simulateFailure: Boolean = false
  ) {
    viewModelScope.launch {
      _checkoutStep.value = BookingCheckoutStep.VerifyingSignature(
        "Backend verifying payment signature & webhook (HMAC-SHA256 & idempotency)..."
      )
      delay(1100) // realistic webhook turnaround

      if (simulateFailure) {
        _checkoutStep.value = BookingCheckoutStep.Failure(
          "Payment verification failed: Gateway reported user abort or insufficient bank balance."
        )
        return@launch
      }

      val booking = repository.verifyPaymentAndConfirmBooking(
        order = gatewayCheckout.order,
        customerUid = _userProfile.value.uid,
        customerName = _userProfile.value.name,
        customerPhone = _userProfile.value.phone,
        studio = gatewayCheckout.studio,
        pkg = gatewayCheckout.pkg,
        eventType = gatewayCheckout.eventType,
        eventAt = gatewayCheckout.eventAt,
        venue = gatewayCheckout.venue,
        notes = gatewayCheckout.notes,
        chosenMethod = paymentMethod
      )

      _checkoutStep.value = BookingCheckoutStep.BookingSuccess(booking, gatewayCheckout.order)
      _statusMessage.value = "Booking #${booking.id} confirmed! Studio payout initiated."
    }
  }

  fun cancelBooking(booking: Booking, reason: String) {
    viewModelScope.launch {
      repository.cancelBooking(booking, reason)
      _statusMessage.value = "Booking #${booking.id} cancelled. Refund initiated to source method."
    }
  }

  fun assignPhotographer(booking: Booking, photographer: PhotographerMember) {
    viewModelScope.launch {
      repository.assignPhotographerToBooking(booking, photographer)
      _statusMessage.value = "${photographer.name} assigned to booking #${booking.id}!"
    }
  }

  fun renewMembership(studioId: String) {
    viewModelScope.launch {
      val mem = repository.renewMembershipSubscription(studioId)
      _statusMessage.value = "Membership renewed (₹500.00). Active until ${mem.expiryDate}!"
    }
  }

  fun requestStudioAudit(studioId: String) {
    viewModelScope.launch {
      repository.requestStudioVerification(studioId)
      _statusMessage.value = "Verification documents submitted for admin compliance audit."
    }
  }
}
