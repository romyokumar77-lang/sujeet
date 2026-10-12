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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContactSupport
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.ZeroSecretsSecurityBadge
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.viewmodel.StudioViewModel

@Composable
fun ComplianceAndLaunchScreen(
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp)
  ) {
    Text(
      text = "Compliance & Production Center",
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = MaterialTheme.colorScheme.onSurface
    )
    Text(
      text = "India IT Rules 2021, RBI Gateway Mandates & Server Architecture",
      fontSize = 12.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.height(14.dp))

    // Grievance Officer Card (Mandatory India IT Rules 2021)
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFFFEF3C7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.ContactSupport, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Column {
            Text(
              text = "Grievance Officer (India IT Rules)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Text(
              text = "Designated statutory officer under Intermediary Guidelines",
              fontSize = 11.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ChecklistLine(label = "Officer Name", value = "Rajeshwar Sen, Chief Compliance Officer")
        ChecklistLine(label = "Email Contact", value = "grievance@bharatstudio.in")
        ChecklistLine(label = "Office Address", value = "Tower B, DLF Cyber City, Sector 25, Gurugram, HR 122002")
        ChecklistLine(label = "Response SLA", value = "Acknowledgement within 24 hours; resolution within 15 days")
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Cancellation & Refund Policy
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFFDCFCE7)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Gavel, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Cancellation & Refund Policy",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "• > 7 Days Prior to Shoot: 100% full refund returned to source UPI/Card.\n• 2 to 7 Days Prior: 50% refund (50% retained for studio lock-in).\n• < 48 Hours: No refund, but 1 complimentary date reschedule permitted.\n• Bad Weather / Natural Force Majeure: 100% credit or rescheduled date.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 18.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Production Security Architecture
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = RoyalNavy)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(24.dp))
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Payment & Secret Security Policy",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "1. Zero Client Secrets: No Razorpay/Cashfree API secrets, private keys or payout credentials exist in the client APK.\n2. Server-side Gateway Order: Client only requests orderId from trusted backend.\n3. HMAC-SHA256 Webhook Verification: Booking is marked PAID only when server verifies gateway webhook signature.\n4. Idempotency Key: Prevents double charges on intermittent 4G/5G connections.\n5. Automated Payouts: Initiated via server backend with KYC-verified studio bank accounts.",
          color = Color(0xFFE5E7EB),
          fontSize = 11.sp,
          lineHeight = 16.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // Google Maps & Geo Policy Card
    Card(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(CircleShape)
              .background(Color(0xFFE0E7FF)),
            contentAlignment = Alignment.Center
          ) {
            Icon(Icons.Default.Map, contentDescription = null, tint = RoyalNavy, modifier = Modifier.size(20.dp))
          }
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Google Maps & Geospatial Routing",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "• Maps SDK for Android meta-data key configured in AndroidManifest.xml.\n• Studio discovery uses verified coordinates with external Google Maps navigation intent fallback.\n• Live photographer tracking respects privacy consent and retention limits.",
          fontSize = 12.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 18.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))
    ZeroSecretsSecurityBadge(modifier = Modifier.fillMaxWidth())
  }
}

@Composable
private fun ChecklistLine(label: String, value: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 3.dp),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = value,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colorScheme.onSurface
    )
  }
}
