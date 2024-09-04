package com.foodieco.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Kitchen
import androidx.compose.material.icons.outlined.NoMeals
import androidx.compose.material.icons.outlined.RoomService
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.foodieco.R
import com.foodieco.ui.theme.capriolaFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
    onDismissRequest: () -> Unit,
    selectedCuisinesFilters: List<String>,
    addToSelectedCuisinesFilters: (String) -> Unit,
    removeFromSelectedCuisinesFilters: (String) -> Unit,
    selectedDietsFilters: List<String>,
    addToSelectedDietsFilters: (String) -> Unit,
    removeFromSelectedDietsFilters: (String) -> Unit,
    selectedIntolerancesFilters: List<String>,
    addToSelectedIntolerancesFilters: (String) -> Unit,
    removeFromSelectedIntolerancesFilters: (String) -> Unit,
    clearSelectedFilters: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column(
            Modifier
                .padding(22.dp)
                .verticalScroll(rememberScrollState())
        ) {
            FilterRow(
                leadingIcon = {
                    Icon(
                        Icons.Outlined.RoomService,
                        "Cuisine icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = "Cuisines",
                allFilters = stringArrayResource(id = R.array.cuisines),
                selectedFilters = selectedCuisinesFilters,
                addToSelectedFilters = addToSelectedCuisinesFilters,
                removeFromSelectedFilters = removeFromSelectedCuisinesFilters
            )
            HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
            FilterRow(
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Kitchen,
                        "Diets icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = "Diets",
                allFilters = stringArrayResource(id = R.array.diets),
                selectedFilters = selectedDietsFilters,
                addToSelectedFilters = addToSelectedDietsFilters,
                removeFromSelectedFilters = removeFromSelectedDietsFilters
            )
            HorizontalDivider(modifier = Modifier.padding(top = 6.dp, bottom = 14.dp))
            FilterRow(
                leadingIcon = {
                    Icon(
                        Icons.Outlined.NoMeals,
                        "Intolerances icon",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                title = "Intolerances",
                allFilters = stringArrayResource(id = R.array.intolerances),
                selectedFilters = selectedIntolerancesFilters,
                addToSelectedFilters = addToSelectedIntolerancesFilters,
                removeFromSelectedFilters = removeFromSelectedIntolerancesFilters
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Button(onClick = clearSelectedFilters) {
                    Text("Clear all")
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterRow(
    leadingIcon: @Composable () -> Unit,
    title: String,
    allFilters: Array<String>,
    selectedFilters: List<String>,
    addToSelectedFilters: (String) -> Unit,
    removeFromSelectedFilters: (String) -> Unit
) {
    Row(modifier = Modifier.padding(bottom = 6.dp)) {
        leadingIcon()
        Text(
            title,
            fontFamily = capriolaFontFamily,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
    FlowRow {
        allFilters.forEach { filter ->
            val isSelected = selectedFilters.contains(filter)
            FilterChip(
                selected = isSelected,
                onClick = {
                    when {
                        isSelected -> removeFromSelectedFilters(filter)
                        else -> addToSelectedFilters(filter)
                    }
                },
                leadingIcon = {
                    AnimatedVisibility(isSelected) {
                        Icon(
                            Icons.Outlined.Check,
                            "Check icon",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                label = { Text(filter.capitalize(Locale.current)) },
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}
