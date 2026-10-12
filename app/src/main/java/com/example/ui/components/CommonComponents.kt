package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
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
import com.example.data.model.BookingStatus
import com.example.data.model.PaymentStatus
import com.example.data.model.PayoutStatus
import com.example.data.model.VerificationStatus
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.RubyDanger
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronLight

@Composable
fun StudioVerificationBadge(status: VerificationStatus, modifier: Modifier = Modifier) {
  val (bgColor, textColor, text) = when (status) {
    VerificationStatus.VERIFIED -> Triple(Color(0xFFE6F4EA), EmeraldSuccess, "VERIFIED PARTNER")
    VerificationStatus.PENDING_VERIFICATION -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "AUDIT PENDING")
    VerificationStatus.UNVERIFIED -> Triple(Color(0xFFFEE2E2), RubyDanger, "UNVERIFIED")
  }

  Row(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(bgColor)
      .padding(horizontal = 8.dp, vertical = 3.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = if (status == VerificationStatus.VERIFIED) Icons.Default.CheckCircle else Icons.Default.Info,
      contentDescription = text,
      tint = textColor,
      modifier = Modifier.size(12.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = text,
      fontSize = 10.sp,
      fontWeight = FontWeight.Bold,
      color = textColor,
      letterSpacing = 0.5.sp
    )
  }
}

@Composable
fun RatingBadge(rating: Double, reviewCount: Int, modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .clip(RoundedCornerShape(6.dp))
      .background(Color(0xFF1E1B4B))
      .padding(horizontal = 8.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.Star,
      contentDescription = "Rating",
      tint = Color(0xFFFBBF24),
      modifier = Modifier.size(14.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = String.format(java.util.Locale.US, "%.1f", rating),
      color = Color.White,
      fontSize = 12.sp,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = " ($reviewCount)",
      color = Color(0xFFD1D5DB),
      fontSize = 11.sp
    )
  }
}

@Composable
fun StatusPill(status: BookingStatus, modifier: Modifier = Modifier) {
  val (bgColor, textColor, label) = when (status) {
    BookingStatus.CONFIRMED -> Triple(Color(0xFFE0E7FF), Color(0xFF3730A3), "CONFIRMED")
    BookingStatus.PHOTOGRAPHER_ASSIGNED -> Triple(Color(0xFFEDE9FE), Color(0xFF6D28D9), "PHOTOGRAPHER ALLOCATED")
    BookingStatus.SHOOT_IN_PROGRESS -> Triple(Color(0xFFFEF3C7), Color(0xFFB45309), "SHOOT IN PROGRESS")
    BookingStatus.EDITING_PHASE -> Triple(Color(0xFFCFFAFE), Color(0xFF0E7490), "POST-PROCESSING")
    BookingStatus.DELIVERED -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "RAW & EDITS DELIVERED")
    BookingStatus.CANCELLED -> Triple(Color(0xFFFEE2E2), RubyDanger, "CANCELLED")
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(bgColor)
      .padding(horizontal = 10.dp, vertical = 4.dp)
  ) {
    Text(
      text = label,
      color = textColor,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold
    )
  }
}

@Composable
fun PaymentStatusPill(status: PaymentStatus, modifier: Modifier = Modifier) {
  val (bgColor, textColor, label) = when (status) {
    PaymentStatus.PAYMENT_VERIFIED -> Triple(Color(0xFFDCFCE7), EmeraldSuccess, "PAID & VERIFIED")
    PaymentStatus.ORDER_CREATED -> Triple(Color(0xFFFEF3C7), SaffronDark, "GATEWAY ORDER CREATED")
    PaymentStatus.REFUND_REQUESTED -> Triple(Color(0xFFFFEDD5), Color(0xFFC2410C), "REFUND REQUESTED")
    PaymentStatus.REFUNDED -> Triple(Color(0xFFF3F4F6), Color(0xFF4B5563), "REFUNDED")
    PaymentStatus.FAILED -> Triple(Color(0xFFFEE2E2), RubyDanger, "FAILED")
  }

  Text(
    text = label,
    color = textColor,
    fontSize = 11.sp,
    fontWeight = FontWeight.SemiBold,
    modifier = modifier
      .clip(RoundedCornerShape(4.dp))
      .background(bgColor)
      .padding(horizontal = 6.dp, vertical = 2.dp)
  )
}

@Composable
fun PayoutStatusPill(status: PayoutStatus, modifier: Modifier = Modifier) {
  val (bgColor, textColor, label) = when (status) {
    PayoutStatus.INITIATED -> Triple(Color(0xFFFEF3C7), SaffronDark, "INITIATED")
    PayoutStatus.IN_TRANSIT -> Triple(Color(0xFFE0E7FF), Color(0xFF4338CA), "IN TRANSIT")
    PayoutStatus.SETTLED_TO_BANK -> Triple(Color(0xFFDCFCE7), EmeraldSuccess, "SETTLED TO BANK")
  }

  Text(
    text = label,
    color = textColor,
    fontSize = 11.sp,
    fontWeight = FontWeight.Bold,
    modifier = modifier
      .clip(RoundedCornerShape(4.dp))
      .background(bgColor)
      .padding(horizontal = 6.dp, vertical = 2.dp)
  )
}

@Composable
fun ZeroSecretsSecurityBadge(modifier: Modifier = Modifier) {
  Row(
    modifier = modifier
      .clip(RoundedCornerShape(8.dp))
      .background(Color(0xFF1E1B4B))
      .border(1.dp, SaffronDark, RoundedCornerShape(8.dp))
      .padding(horizontal = 10.dp, vertical = 6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.Lock,
      contentDescription = "PCI-DSS Security",
      tint = SaffronLight,
      modifier = Modifier.size(14.dp)
    )
    Spacer(modifier = Modifier.width(6.dp))
    Text(
      text = "Zero Secret Keys on Client • PCI-DSS • HMAC Webhook Verified",
      color = Color.White,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium
    )
  }
}
