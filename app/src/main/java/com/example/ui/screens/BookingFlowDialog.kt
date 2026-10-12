package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.StudioPackage
import com.example.ui.components.ZeroSecretsSecurityBadge
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RubyDanger
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.BookingCheckoutStep
import com.example.viewmodel.StudioViewModel

@Composable
fun BookingFlowDialog(
  viewModel: StudioViewModel,
  step: BookingCheckoutStep,
  onDismiss: () -> Unit
) {
  if (step is BookingCheckoutStep.Idle) return

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Surface(
      modifier = Modifier
        .fillMaxWidth(0.94f)
        .padding(vertical = 24.dp),
      shape = RoundedCornerShape(24.dp),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(20.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color(0xFFFEF3C7)),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Security,
                contentDescription = null,
                tint = SaffronDark,
                modifier = Modifier.size(20.dp)
              )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Secure Studio Booking",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "India Gateway Verification Flow",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(onClick = onDismiss, modifier = Modifier.testTag("dialog_close_btn")) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        when (step) {
          is BookingCheckoutStep.FormInput -> {
            FormInputView(
              step = step,
              onSubmit = { eventType, date, venue, notes ->
                viewModel.requestBackendOrderAndOpenGateway(
                  studio = step.studio,
                  pkg = step.selectedPackage,
                  eventType = eventType,
                  eventAt = date,
                  venue = venue,
                  notes = notes
                )
              }
            )
          }

          is BookingCheckoutStep.CreatingOrder -> {
            LoadingProgressView(
              title = "Initiating Secure Handshake",
              subtitle = step.message,
              badgeText = "Calling trusted backend /create-order..."
            )
          }

          is BookingCheckoutStep.GatewayCheckout -> {
            GatewayCheckoutView(
              step = step,
              onPay = { method, simulateFailure ->
                viewModel.confirmGatewayPayment(step, method, simulateFailure)
              }
            )
          }

          is BookingCheckoutStep.VerifyingSignature -> {
            LoadingProgressView(
              title = "Verifying Gateway Webhook",
              subtitle = step.message,
              badgeText = "HMAC-SHA256 signature match & idempotency check"
            )
          }

          is BookingCheckoutStep.BookingSuccess -> {
            BookingSuccessView(
              step = step,
              onDone = {
                viewModel.dismissBookingFlow()
                viewModel.navigateTo(com.example.viewmodel.AppScreen.MY_BOOKINGS)
              }
            )
          }

          is BookingCheckoutStep.Failure -> {
            BookingFailureView(
              error = step.error,
              onRetry = onDismiss
            )
          }

          else -> {}
        }
      }
    }
  }
}

@Composable
private fun FormInputView(
  step: BookingCheckoutStep.FormInput,
  onSubmit: (String, String, String, String) -> Unit
) {
  var eventType by remember { mutableStateOf("Royal Wedding & Reception") }
  var eventDate by remember { mutableStateOf("15 Dec 2026 • 04:00 PM") }
  var venueAddress by remember { mutableStateOf("The Leela Palace, Courtyard Pavilion, " + step.studio.city) }
  var notes by remember { mutableStateOf("Drone shots for grand baraat entry and candid couple session during sunset.") }

  Column(modifier = Modifier.fillMaxWidth()) {
    // Package preview summary
    Card(
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = step.studio.name,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
          Text(
            text = "₹${String.format(java.util.Locale.US, "%,.0f", step.selectedPackage.price)}",
            fontWeight = FontWeight.ExtraBold,
            color = RoyalNavy,
            fontSize = 16.sp
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Selected: ${step.selectedPackage.title} (${step.selectedPackage.durationHours})",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = step.selectedPackage.inclusions,
          fontSize = 11.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Event Details",
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = eventType,
      onValueChange = { eventType = it },
      label = { Text("Event Type") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_event_type")
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = eventDate,
      onValueChange = { eventDate = it },
      label = { Text("Event Date & Time") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_event_date")
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = venueAddress,
      onValueChange = { venueAddress = it },
      label = { Text("Shoot Venue Address") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_venue_address")
    )

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
      value = notes,
      onValueChange = { notes = it },
      label = { Text("Special Requests / Gear Notes") },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("input_notes")
    )

    Spacer(modifier = Modifier.height(14.dp))

    ZeroSecretsSecurityBadge(modifier = Modifier.fillMaxWidth())

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = { onSubmit(eventType, eventDate, venueAddress, notes) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("request_order_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = SaffronPrimary,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Text(
        text = "Request Booking Order from Backend",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp)
      )
    }
  }
}

@Composable
private fun LoadingProgressView(title: String, subtitle: String, badgeText: String) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 28.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    CircularProgressIndicator(
      color = SaffronPrimary,
      strokeWidth = 3.dp,
      modifier = Modifier.size(48.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 16.sp,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(6.dp))
    Text(
      text = subtitle,
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Box(
      modifier = Modifier
        .clip(RoundedCornerShape(6.dp))
        .background(Color(0xFFFEF3C7))
        .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
      Text(
        text = badgeText,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        color = SaffronDark
      )
    }
  }
}

@Composable
private fun GatewayCheckoutView(
  step: BookingCheckoutStep.GatewayCheckout,
  onPay: (String, Boolean) -> Unit
) {
  var selectedMethod by remember { mutableStateOf("UPI_GPAY") }
  var simulateFailure by remember { mutableStateOf(false) }

  Column(modifier = Modifier.fillMaxWidth()) {
    // Payment Gateway brand header
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(Color(0xFF0F172A))
        .padding(14.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "BHARATSTUDIO CHECKOUT",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF94A3B8),
            letterSpacing = 1.sp
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "Order: ${step.order.gatewayOrderId}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
        Text(
          text = "₹${String.format(java.util.Locale.US, "%,.0f", step.order.amount)}",
          fontSize = 18.sp,
          fontWeight = FontWeight.ExtraBold,
          color = Color(0xFFFBBF24)
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Select Payment Method (India Gateway)",
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Payment options
    PaymentOptionRow(
      title = "UPI - Google Pay / PhonePe / Paytm",
      subtitle = "Instant VPA transfer with zero surcharge",
      icon = Icons.Default.PhoneAndroid,
      selected = selectedMethod == "UPI_GPAY",
      onSelect = { selectedMethod = "UPI_GPAY" }
    )

    PaymentOptionRow(
      title = "Credit / Debit Card (Visa / RuPay / Mastercard)",
      subtitle = "End-to-end tokenized via RBI compliant vault",
      icon = Icons.Default.CreditCard,
      selected = selectedMethod == "CARDS",
      onSelect = { selectedMethod = "CARDS" }
    )

    PaymentOptionRow(
      title = "NetBanking (HDFC, SBI, ICICI, Axis)",
      subtitle = "Direct authorization from 50+ Indian banks",
      icon = Icons.Default.AccountBalance,
      selected = selectedMethod == "NETBANKING",
      onSelect = { selectedMethod = "NETBANKING" }
    )

    Spacer(modifier = Modifier.height(12.dp))

    // Simulation toggle for testing edge cases
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(10.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "Test Sandbox: Simulate Payment Failure",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Emulates user cancel, timeout or bank reject",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Switch(
          checked = simulateFailure,
          onCheckedChange = { simulateFailure = it },
          modifier = Modifier.testTag("switch_simulate_failure")
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    Button(
      onClick = { onPay(selectedMethod, simulateFailure) },
      modifier = Modifier
        .fillMaxWidth()
        .testTag("pay_gateway_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = if (simulateFailure) RubyDanger else EmeraldSuccess,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Text(
        text = if (simulateFailure) "Simulate Failed Payment" else "Pay ₹${String.format(java.util.Locale.US, "%,.0f", step.order.amount)} via Gateway",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp)
      )
    }
  }
}

@Composable
private fun PaymentOptionRow(
  title: String,
  subtitle: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  selected: Boolean,
  onSelect: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp)
      .clip(RoundedCornerShape(10.dp))
      .border(
        width = if (selected) 1.5.dp else 1.dp,
        color = if (selected) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant,
        shape = RoundedCornerShape(10.dp)
      )
      .background(if (selected) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surface)
      .clickable(onClick = onSelect)
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = if (selected) SaffronDark else MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = subtitle,
        fontSize = 10.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
    RadioButton(
      selected = selected,
      onClick = onSelect
    )
  }
}

@Composable
private fun BookingSuccessView(
  step: BookingCheckoutStep.BookingSuccess,
  onDone: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .clip(CircleShape)
        .background(Color(0xFFDCFCE7)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.CheckCircle,
        contentDescription = "Success",
        tint = EmeraldSuccess,
        modifier = Modifier.size(36.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Booking Confirmed & Verified!",
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = "Gateway Signature Verified • Idempotency Checked",
      fontSize = 12.sp,
      color = EmeraldSuccess,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Details card
    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Booking Ref", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("#${step.booking.id}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Studio", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(step.booking.studioName, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Assigned Photographer", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(step.booking.assignedPhotographerName ?: "Allocating...", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SaffronDark)
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("Total Paid", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text("₹${String.format(java.util.Locale.US, "%,.0f", step.booking.amount)}", fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = RoyalNavy)
        }
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Payout explanation note
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(Color(0xFFFEF3C7))
        .padding(10.dp)
    ) {
      Text(
        text = "Backend Payout Engine: 95% net payout (₹${String.format(java.util.Locale.US, "%,.0f", step.booking.amount * 0.95)}) automatically logged and queued for studio settlement.",
        fontSize = 11.sp,
        color = Color(0xFF92400E)
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    Button(
      onClick = onDone,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("view_my_bookings_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = SaffronPrimary,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Text(
        text = "View in My Bookings",
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 4.dp)
      )
    }
  }
}

@Composable
private fun BookingFailureView(error: String, onRetry: () -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .clip(CircleShape)
        .background(Color(0xFFFEE2E2)),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.Error,
        contentDescription = "Failure",
        tint = RubyDanger,
        modifier = Modifier.size(36.dp)
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    Text(
      text = "Payment Verification Aborted",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = error,
      fontSize = 12.sp,
      color = RubyDanger,
      textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )

    Spacer(modifier = Modifier.height(18.dp))

    Button(
      onClick = onRetry,
      modifier = Modifier
        .fillMaxWidth()
        .testTag("retry_booking_btn"),
      colors = ButtonDefaults.buttonColors(
        containerColor = SaffronDark,
        contentColor = Color.White
      ),
      shape = RoundedCornerShape(12.dp)
    ) {
      Text("Close & Try Again")
    }
  }
}
