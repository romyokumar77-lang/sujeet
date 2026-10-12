package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.StudioCard
import com.example.ui.theme.RoyalNavy
import com.example.ui.theme.SaffronDark
import com.example.ui.theme.SaffronPrimary
import com.example.viewmodel.StudioViewModel

@Composable
fun HomeScreen(
  viewModel: StudioViewModel,
  modifier: Modifier = Modifier
) {
  val studios by viewModel.filteredStudios.collectAsState()
  val searchQuery by viewModel.searchQuery.collectAsState()
  val selectedCity by viewModel.selectedCity.collectAsState()
  val selectedCategory by viewModel.selectedCategory.collectAsState()
  val userProfile by viewModel.userProfile.collectAsState()

  val cities = listOf("All Cities", "Jaipur", "Mumbai", "Delhi NCR", "Bengaluru", "Varanasi")
  val categories = listOf("All", "Wedding", "Pre-Wedding", "Drone", "Portfolio", "Maternity")

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 80.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Banner Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
        ) {
          Image(
            painter = painterResource(id = R.drawable.hero_studio_banner_1791779779806),
            contentDescription = "BharatStudio Wedding Hero",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.3f),
                    Color.Black.copy(alpha = 0.85f)
                  )
                )
              )
          )
          Column(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .padding(16.dp)
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFFFEF3C7))
                .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
              Text(
                text = "Namaste ${userProfile.name} • ${userProfile.language}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronDark
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Capture Life's Grandest Milestones",
              color = Color.White,
              fontSize = 18.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Verified Indian Photography Studios with Zero-Secret Secure Gateway",
              color = Color(0xFFE5E7EB),
              fontSize = 11.sp
            )
          }
        }
      }
    }

    // Search bar
    item {
      Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { viewModel.setSearchQuery(it) },
          placeholder = { Text("Search studio name, city, or event (e.g. Haldi, Drone)...", fontSize = 12.sp) },
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = SaffronDark)
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.setSearchQuery("") }) {
                Icon(Icons.Default.Clear, contentDescription = "Clear")
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(14.dp),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("search_studios_input")
        )
      }
    }

    // City filters
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.LocationCity, contentDescription = null, tint = SaffronDark, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Select City",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          cities.forEach { city ->
            val isSelected = selectedCity == city
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setSelectedCity(city) },
              label = { Text(city, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = SaffronPrimary,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("filter_city_$city")
            )
          }
        }
      }
    }

    // Category filters
    item {
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(Icons.Default.FilterList, contentDescription = null, tint = RoyalNavy, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Service Categories",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
        }
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          categories.forEach { cat ->
            val isSelected = selectedCategory == cat
            FilterChip(
              selected = isSelected,
              onClick = { viewModel.setSelectedCategory(cat) },
              label = { Text(cat, fontSize = 11.sp) },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = RoyalNavy,
                selectedLabelColor = Color.White
              ),
              modifier = Modifier.testTag("filter_cat_$cat")
            )
          }
        }
      }
    }

    // Results Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Featured Verified Studios (${studios.size})",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurface
        )
      }
    }

    // Studio List Items
    if (studios.isEmpty()) {
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
              text = "No studios found matching your search.",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Try clearing filters or selecting 'All Cities'.",
              fontSize = 12.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      items(studios) { studio ->
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
          StudioCard(
            studio = studio,
            onStudioClick = { viewModel.openStudioDetail(studio) },
            onBookClick = {
              val firstPkg = studio.packages.firstOrNull()
              if (firstPkg != null) {
                viewModel.startBookingFlow(studio, firstPkg)
              } else {
                viewModel.openStudioDetail(studio)
              }
            }
          )
        }
      }
    }
  }
}
