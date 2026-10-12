package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Studio
import com.example.data.model.StudioPackage
import com.example.ui.components.RatingBadge
import com.example.ui.components.StudioVerificationBadge
import com.example.ui.components.ZeroSecretsSecurityBadge
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioViewModel

@Composable
fun StudioDetailScreen(
  studio: Studio,
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  BackHandler {
    viewModel.navigateTo(AppScreen.EXPLORE)
  }

  var selectedPackageIndex by remember { mutableStateOf(0) }
  val activePackage = studio.packages.getOrNull(selectedPackageIndex) ?: studio.packages.firstOrNull()

  Box(modifier = modifier.fillMaxSize()) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(bottom = 90.dp)
    ) {
      // Hero Header with Image
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(260.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.hero_studio_banner_1791779779806),
          contentDescription = "Studio setup",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.5f),
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.85f)
                )
              )
            )
        )

        // Top App Bar
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 36.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          IconButton(
            onClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(Color.Black.copy(alpha = 0.45f))
              .testTag("detail_back_btn")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back",
              tint = Color.White
            )
          }

          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
              onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, "Check out ${studio.name} on BharatStudio: ${studio.address}")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Studio"))
              },
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.45f))
            ) {
              Icon(Icons.Default.Share, contentDescription = "Share", tint = Color.White)
            }
          }
        }

        // Title at bottom of banner
        Column(
          modifier = Modifier
            .align(Alignment.BottomStart)
            .padding(16.dp)
        ) {
          StudioVerificationBadge(status = studio.verificationStatus)
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = studio.name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
          Text(
            text = "${studio.city} • Verified Partner until ${studio.membershipExpiry}",
            fontSize = 12.sp,
            color = Color(0xFFF3F4F6)
          )
        }
      }

      // Content Body
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      ) {
        // Stats row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          RatingBadge(rating = studio.rating, reviewCount = studio.reviewCount)

          Text(
            text = studio.priceRange,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = RoyalNavy
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Google Maps & Contact Card
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.Top) {
              Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = SaffronDark,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Studio Address & Geo Coordinates",
                  fontSize = 12.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = studio.address,
                  fontSize = 12.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                  text = "Lat: ${studio.geoLat}, Lng: ${studio.geoLng}",
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action buttons: Maps Intent & Call Intent
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Button(
                onClick = {
                  openGoogleMapsIntent(context, studio)
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("btn_open_google_maps"),
                colors = ButtonDefaults.buttonColors(
                  containerColor = RoyalNavy,
                  contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Google Maps", fontSize = 12.sp, fontWeight = FontWeight.Bold)
              }

              OutlinedButton(
                onClick = {
                  val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${studio.phone}"))
                  context.startActivity(dialIntent)
                },
                modifier = Modifier
                  .weight(1f)
                  .testTag("btn_call_studio"),
                shape = RoundedCornerShape(10.dp)
              ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Call Studio", fontSize = 12.sp)
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Description
        Text(
          text = "About This Studio",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = studio.description,
          fontSize = 13.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = 19.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Packages Section
        Text(
          text = "Choose Photography Package",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        studio.packages.forEachIndexed { index, pkg ->
          val isSelected = selectedPackageIndex == index
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp)
              .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) SaffronPrimary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
              )
              .clickable { selectedPackageIndex = index }
              .testTag("package_card_$index"),
            colors = CardDefaults.cardColors(
              containerColor = if (isSelected) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(12.dp)
          ) {
            Column(modifier = Modifier.padding(14.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = pkg.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "Duration: ${pkg.durationHours}",
                    fontSize = 11.sp,
                    color = SaffronDark,
                    fontWeight = FontWeight.Medium
                  )
                }
                Text(
                  text = "₹${String.format(java.util.Locale.US, "%,.0f", pkg.price)}",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 16.sp,
                  color = RoyalNavy
                )
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = pkg.inclusions,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Staff Photographers roster
        Text(
          text = "Certified Photographer Team (${studio.photographers.size})",
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
                Icon(
                  imageVector = Icons.Default.Camera,
                  contentDescription = null,
                  tint = RoyalNavy,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = photographer.name,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${photographer.specialization} • Gear: ${photographer.gear}",
                  fontSize = 11.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(Color(0xFFDCFCE7))
                  .padding(horizontal = 6.dp, vertical = 2.dp)
              ) {
                Text(
                  text = "AVAILABLE",
                  color = Color(0xFF15803D),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))
        ZeroSecretsSecurityBadge(modifier = Modifier.fillMaxWidth())
      }
    }

    // Pinned Bottom Booking Bar
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .navigationBarsPadding(),
      shadowElevation = 8.dp,
      color = MaterialTheme.colorScheme.surface
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Total Package",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = "₹${String.format(java.util.Locale.US, "%,.0f", activePackage?.price ?: studio.basePrice)}",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = RoyalNavy
          )
        }

        Button(
          onClick = {
            if (activePackage != null) {
              viewModel.startBookingFlow(studio, activePackage)
            }
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = SaffronPrimary,
            contentColor = Color.White
          ),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("btn_proceed_booking")
        ) {
          Text("Proceed to Book", fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

private fun openGoogleMapsIntent(context: Context, studio: Studio) {
  try {
    val geoUri = Uri.parse("geo:${studio.geoLat},${studio.geoLng}?q=${Uri.encode("${studio.name}, ${studio.address}")}")
    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
      setPackage("com.google.android.apps.maps")
    }
    if (mapIntent.resolveActivity(context.packageManager) != null) {
      context.startActivity(mapIntent)
    } else {
      // Fallback to browser Google Maps URL
      val browserUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${studio.geoLat},${studio.geoLng}")
      val webIntent = Intent(Intent.ACTION_VIEW, browserUri)
      context.startActivity(webIntent)
    }
  } catch (e: Exception) {
    Toast.makeText(context, "Opening Google Maps for ${studio.name}", Toast.LENGTH_SHORT).show()
  }
}
