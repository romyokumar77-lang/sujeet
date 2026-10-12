package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.BookingFlowDialog
import com.example.ui.screens.ComplianceAndLaunchScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MyBookingsScreen
import com.example.ui.screens.ProfileAndLanguageDialog
import com.example.ui.screens.StudioDetailScreen
import com.example.ui.screens.StudioPartnerPortalScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.AppScreen
import com.example.viewmodel.StudioViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        BharatStudioApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BharatStudioApp(
  viewModel: StudioViewModel = viewModel()
) {
  val currentScreen by viewModel.currentScreen.collectAsState()
  val selectedStudio by viewModel.selectedStudio.collectAsState()
  val checkoutStep by viewModel.checkoutStep.collectAsState()
  val statusMessage by viewModel.statusMessage.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()

  var showProfileDialog by remember { mutableStateOf(false) }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    contentWindowInsets = WindowInsets(0, 0, 0, 0),
    topBar = {
      if (currentScreen != AppScreen.STUDIO_DETAIL) {
        TopAppBar(
          title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(SaffronPrimary),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.CameraAlt,
                  contentDescription = "BharatStudio Logo",
                  tint = Color.White,
                  modifier = Modifier.size(18.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Column {
                Text(
                  text = "BharatStudio",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 18.sp,
                  color = RoyalNavy
                )
                Text(
                  text = "India Photo Studios & Payouts",
                  fontSize = 10.sp,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          },
          actions = {
            IconButton(
              onClick = { showProfileDialog = true },
              modifier = Modifier.testTag("btn_profile_dialog")
            ) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(Color(0xFFFEF3C7)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = "Profile & Settings",
                  tint = SaffronDark,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          )
        )
      }
    },
    bottomBar = {
      if (currentScreen != AppScreen.STUDIO_DETAIL) {
        NavigationBar(
          containerColor = MaterialTheme.colorScheme.surface,
          tonalElevation = 8.dp
        ) {
          NavigationBarItem(
            selected = currentScreen == AppScreen.EXPLORE,
            onClick = { viewModel.navigateTo(AppScreen.EXPLORE) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "Explore") },
            label = { Text("Explore", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = RoyalNavy,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_explore")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.MY_BOOKINGS,
            onClick = { viewModel.navigateTo(AppScreen.MY_BOOKINGS) },
            icon = { Icon(Icons.Default.EventNote, contentDescription = "My Bookings") },
            label = { Text("Bookings", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = RoyalNavy,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_bookings")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.PARTNER_PORTAL,
            onClick = { viewModel.navigateTo(AppScreen.PARTNER_PORTAL) },
            icon = { Icon(Icons.Default.Store, contentDescription = "Partner") },
            label = { Text("Studio Partner", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = RoyalNavy,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_partner")
          )

          NavigationBarItem(
            selected = currentScreen == AppScreen.COMPLIANCE_AND_LAUNCH,
            onClick = { viewModel.navigateTo(AppScreen.COMPLIANCE_AND_LAUNCH) },
            icon = { Icon(Icons.Default.Gavel, contentDescription = "Compliance") },
            label = { Text("Compliance", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = RoyalNavy,
              indicatorColor = Color(0xFFFEF3C7)
            ),
            modifier = Modifier.testTag("nav_compliance")
          )
        }
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      when (currentScreen) {
        AppScreen.EXPLORE -> {
          HomeScreen(viewModel = viewModel)
        }
        AppScreen.STUDIO_DETAIL -> {
          val studio = selectedStudio
          if (studio != null) {
            StudioDetailScreen(studio = studio, viewModel = viewModel)
          } else {
            HomeScreen(viewModel = viewModel)
          }
        }
        AppScreen.MY_BOOKINGS -> {
          MyBookingsScreen(viewModel = viewModel)
        }
        AppScreen.PARTNER_PORTAL -> {
          StudioPartnerPortalScreen(viewModel = viewModel)
        }
        AppScreen.COMPLIANCE_AND_LAUNCH -> {
          ComplianceAndLaunchScreen(viewModel = viewModel)
        }
      }

      // Status banner if any
      if (statusMessage != null) {
        Box(
          modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F172A))
            .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = statusMessage ?: "",
              color = Color.White,
              fontSize = 12.sp,
              modifier = Modifier.weight(1f)
            )
            IconButton(
              onClick = { viewModel.clearStatusMessage() },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color.White, modifier = Modifier.size(16.dp))
            }
          }
        }
      }
    }
  }

  // Booking Flow Dialog
  BookingFlowDialog(
    viewModel = viewModel,
    step = checkoutStep,
    onDismiss = { viewModel.dismissBookingFlow() }
  )

  // Profile & Language Dialog
  if (showProfileDialog) {
    ProfileAndLanguageDialog(
      viewModel = viewModel,
      onDismiss = { showProfileDialog = false }
    )
  }
}
