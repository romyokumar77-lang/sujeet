package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.PayoutStatusPill
import com.example.ui.components.StudioVerificationBadge
import com.example.ui.components.ZeroSecretsSecurityBadge
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.StudioViewModel

@Composable
fun StudioPartnerPortalScreen(
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  val studios by viewModel.studios.collectAsState()
  val studio = studios.firstOrNull() ?: return
  val payouts by viewModel.payouts.collectAsState()
  val bookings by viewModel.bookings.collectAsState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    // Top Partner Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = RoyalNavy)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = "STUDIO PARTNER CONSOLE",
              color = Color(0xFFFBBF24),
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp
            )
            Text(
              text = studio.name,
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
          }
          StudioVerificationBadge(status = studio.verificationStatus)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Owner UID: ${studio.ownerUid} • ${studio.city}",
          color = Color(0xFFD1D5DB),
          fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
          onClick = { viewModel.requestStudioAudit(studio.id) },
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("btn_request_verification")
        ) {
          Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Submit Business KYC & Re-Verify", color = Color.White, fontSize = 11.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // ₹500/month Membership Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
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
              Icon(Icons.Default.CreditCard, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "BharatStudio Partner Membership",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
              )
              Text(
                text = "₹500 / month flat subscription",
                fontSize = 11.sp,
                color = SaffronDark,
                fontWeight = FontWeight.SemiBold
              )
            }
          }

          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(6.dp))
              .background(Color(0xFFDCFCE7))
              .padding(horizontal = 8.dp, vertical = 3.dp)
          ) {
            Text("ACTIVE", color = Color(0xFF15803D), fontSize = 10.sp, fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Subscription valid until ${studio.membershipExpiry}. Grants verified partner listing, client lead notifications, and 0% payout processing surcharges.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
          onClick = { viewModel.renewMembership(studio.id) },
          colors = ButtonDefaults.buttonColors(
            containerColor = SaffronPrimary,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("btn_renew_membership")
        ) {
          Text("Renew Subscription (₹500 via Gateway)", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Studio Payouts Ledger
    Text(
      text = "Studio Payouts Ledger (Marketplace Backend)",
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp,
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "Automated settlements after verified gateway webhook",
      fontSize = 11.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(10.dp))

    payouts.forEach { payout ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Payout #${payout.payoutId}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
              Text(
                text = "Linked Booking: #${payout.bookingId} • ${payout.timestamp}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            PayoutStatusPill(status = payout.status)
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "Gross Booking: ₹${String.format(java.util.Locale.US, "%,.0f", payout.grossAmount)}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(text = "Platform Fee (5%): -₹${String.format(java.util.Locale.US, "%,.0f", payout.platformFee)}", fontSize = 11.sp, color = Color(0xFFDC2626))
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "Net Payout", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
              Text(
                text = "₹${String.format(java.util.Locale.US, "%,.0f", payout.netAmount)}",
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = EmeraldSuccess
              )
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "Settlement Bank: ${payout.bankAccountMasked} (Gateway Ref: ${payout.gatewayPayoutId})",
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Photographer Team & Assignments
    Text(
      text = "Studio Photographer Team (${studio.photographers.size})",
      fontWeight = FontWeight.Bold,
      fontSize = 15.sp,
      color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    studio.photographers.forEach { photographer ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFFE0E7FF)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = RoyalNavy, modifier = Modifier.size(18.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = photographer.name,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
            Text(
              text = "${photographer.phone} • ${photographer.gear}",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          // Assign to any pending booking action
          val unassignedBooking = bookings.firstOrNull { it.assignedPhotographerUid == null }
          if (unassignedBooking != null) {
            Button(
              onClick = { viewModel.assignPhotographer(unassignedBooking, photographer) },
              shape = RoundedCornerShape(6.dp),
              colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
              modifier = Modifier.testTag("btn_assign_${photographer.id}")
            ) {
              Text("Assign", fontSize = 10.sp)
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
    ZeroSecretsSecurityBadge(modifier = Modifier.fillMaxWidth())
  }
}
