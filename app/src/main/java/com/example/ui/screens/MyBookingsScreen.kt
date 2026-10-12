package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.data.model.Booking
import com.example.data.model.BookingStatus
import com.example.ui.components.PaymentStatusPill
import com.example.ui.components.StatusPill
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RubyDanger
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioViewModel

@Composable
fun MyBookingsScreen(
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  val bookings by viewModel.bookings.collectAsState()
  var bookingToCancel by remember { mutableStateOf<Booking?>(null) }
  var cancelReason by remember { mutableStateOf("Family schedule adjustment") }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Text(
      text = "My Event Bookings",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Live status timeline & assigned studio photographers",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(14.dp))

    if (bookings.isEmpty()) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(bottom = 60.dp),
        contentAlignment = Alignment.Center
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            tint = SaffronDark,
            modifier = Modifier.size(54.dp)
          )
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "No Bookings Yet",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "Explore verified studios and book with zero-secret gateway flow.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(16.dp))
          Button(
            onClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
            colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary)
          ) {
            Text("Discover Studios")
          }
        }
      }
    } else {
      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
      ) {
        items(bookings) { booking ->
          BookingItemCard(
            booking = booking,
            onCancelClick = { bookingToCancel = booking }
          )
        }
      }
    }
  }

  // Cancellation & Refund Dialog
  if (bookingToCancel != null) {
    AlertDialog(
      onDismissRequest = { bookingToCancel = null },
      title = {
        Text("Cancel Booking & Refund", fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          Text(
            text = "Per BharatStudio Server-Side Refund Policy:",
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
          )
          Text(
            text = "• 100% refund processed if cancelled >7 days before event.\n• 50% refund processed if cancelled 2-7 days before event.\n• Refunds routed back to original UPI/Card gateway order.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Spacer(modifier = Modifier.height(10.dp))
          OutlinedTextField(
            value = cancelReason,
            onValueChange = { cancelReason = it },
            label = { Text("Reason for cancellation") },
            modifier = Modifier.fillMaxWidth()
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val target = bookingToCancel
            if (target != null) {
              viewModel.cancelBooking(target, cancelReason)
            }
            bookingToCancel = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = RubyDanger)
        ) {
          Text("Confirm Cancellation")
        }
      },
      dismissButton = {
        TextButton(onClick = { bookingToCancel = null }) {
          Text("Keep Booking")
        }
      }
    )
  }
}

@Composable
private fun BookingItemCard(
  booking: Booking,
  onCancelClick: () -> Unit
) {
  Card(
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("booking_card_${booking.id}")
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top row: ID & Status
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Booking #${booking.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Booked on ${booking.createdAt}",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        StatusPill(status = booking.bookingStatus)
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Studio name and package
      Text(
        text = booking.studioName,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = RoyalNavy
      )
      Text(
        text = "${booking.eventType} • ${booking.packageTitle}",
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(10.dp))

      // Date & Venue
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = booking.eventAt, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
      }
      Spacer(modifier = Modifier.height(4.dp))
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = booking.venue, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Assigned photographer card
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier.padding(10.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape)
              .background(Color(0xFFE0E7FF)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = RoyalNavy, modifier = Modifier.size(16.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (booking.assignedPhotographerName != null) "Assigned: ${booking.assignedPhotographerName}" else "Photographer Assignment in Queue",
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
            Text(
              text = if (booking.assignedPhotographerName != null) "Direct dispatch via studio partner" else "Studio partner assigning specialist",
              fontSize = 10.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          PaymentStatusPill(status = booking.paymentStatus)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Financial row & cancellation
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(text = "Amount Paid", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
          Text(
            text = "₹${String.format(java.util.Locale.US, "%,.0f", booking.amount)}",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = RoyalNavy
          )
        }

        if (booking.bookingStatus != BookingStatus.CANCELLED && booking.bookingStatus != BookingStatus.DELIVERED) {
          OutlinedButton(
            onClick = onCancelClick,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.testTag("btn_cancel_${booking.id}")
          ) {
            Icon(Icons.Default.Cancel, contentDescription = null, modifier = Modifier.size(14.dp), tint = RubyDanger)
            Spacer(modifier = Modifier.width(4.dp))
            Text("Cancel Booking", fontSize = 11.sp, color = RubyDanger)
          }
        }
      }
    }
  }
}
