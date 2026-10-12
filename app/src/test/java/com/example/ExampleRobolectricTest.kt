package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.BharatStudioDatabase
import com.example.data.local.StudioEntity
import com.example.data.model.PaymentStatus
import com.example.data.model.StudioPackage
import com.example.data.repository.StudioRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  private lateinit var database: BharatStudioDatabase
  private lateinit var repository: StudioRepository

  @Before
  fun setup() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    database = Room.inMemoryDatabaseBuilder(context, BharatStudioDatabase::class.java)
      .allowMainThreadQueries()
      .build()
    repository = StudioRepository(database.studioDao())
  }

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("BharatStudio", appName)
  }

  @Test
  fun `initial studios populated and retrievable`() = runBlocking {
    repository.ensureInitialData()
    val studios = repository.getStudios().first()
    assertTrue(studios.isNotEmpty())
    val jaipurStudio = studios.find { it.id == "studio_jaipur_01" }
    assertNotNull(jaipurStudio)
    assertEquals("Jaipur", jaipurStudio?.city)
    assertEquals("Rajputana Royal Visuals", jaipurStudio?.name)
  }

  @Test
  fun `secure booking order flow verifies payment and initiates studio payout`() = runBlocking {
    repository.ensureInitialData()
    val studios = repository.getStudios().first()
    val studio = studios.first()
    val pkg = studio.packages.first()

    // 1. Create order
    val order = repository.createGatewayOrder(
      customerUid = "usr_test_1",
      customerName = "Rohan Verma",
      customerPhone = "+91 98765 00000",
      studio = studio,
      pkg = pkg,
      eventType = "Wedding",
      eventAt = "20 Dec 2026",
      venue = "Palace Hotel",
      notes = "Drone coverage"
    )

    assertEquals(PaymentStatus.ORDER_CREATED, order.status)
    assertTrue(order.gatewayOrderId.startsWith("order_in_live_"))

    // 2. Verify payment and confirm
    val booking = repository.verifyPaymentAndConfirmBooking(
      order = order,
      customerUid = "usr_test_1",
      customerName = "Rohan Verma",
      customerPhone = "+91 98765 00000",
      studio = studio,
      pkg = pkg,
      eventType = "Wedding",
      eventAt = "20 Dec 2026",
      venue = "Palace Hotel",
      notes = "Drone coverage",
      chosenMethod = "UPI_GPAY"
    )

    assertEquals(PaymentStatus.PAYMENT_VERIFIED, booking.paymentStatus)

    // 3. Check payout initiated with 5% platform fee deduction
    val payouts = repository.getPayouts().first()
    val latestPayout = payouts.firstOrNull { it.bookingId == order.bookingId }
    assertNotNull(latestPayout)
    assertEquals(pkg.price * 0.05, latestPayout?.platformFee ?: 0.0, 0.01)
    assertEquals(pkg.price * 0.95, latestPayout?.netAmount ?: 0.0, 0.01)
  }

  @Test
  fun `studio membership renewal updates expiry and logs 500 inr payment`() = runBlocking {
    repository.ensureInitialData()
    val mem = repository.renewMembershipSubscription("studio_jaipur_01")
    assertEquals(500.0, mem.amount, 0.01)
    assertEquals("30 Dec 2026", mem.expiryDate)
  }
}
