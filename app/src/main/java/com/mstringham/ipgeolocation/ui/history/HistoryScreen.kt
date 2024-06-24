package com.mstringham.ipgeolocation.ui.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mstringham.ipgeolocation.R
import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.ui.GeolocationTopAppBar
import com.mstringham.ipgeolocation.ui.navigation.NavigationDestination

object HistoryDestination : NavigationDestination {
    override val route = "history"
    override val titleRes = R.string.history_screen_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navigateBack: () -> Unit,
    deleteHistory: () -> Unit,
    modifier: Modifier = Modifier,
    state: HistoryState
) {
    Scaffold(
        topBar = {
            GeolocationTopAppBar(
                title = stringResource(HistoryDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    deleteHistory()
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))

            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_icon_description),
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        when (state) {
            is HistoryState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            is HistoryState.Results -> {
                HistoryItems(
                    itemsList = state.itemList,
                    modifier = Modifier
                        .padding(
                            start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                            end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                            top = innerPadding.calculateTopPadding()
                        )
                )
            }

            is HistoryState.Error -> {
                Text(
                    text = "${state.error?.asString()}",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }


    }
}

@Composable
private fun HistoryItems(
    itemsList: List<Geolocation>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_large))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(
                items = itemsList,
                key = { _, item -> item.id }
            ) { index, item ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(text = item.query)
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = item.formatTimestamp(),
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Text(text = "${item.city} (${item.timezone})")
                }

                if (index < itemsList.lastIndex) {
                    HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun HistoryItemsPreview() {
    HistoryItems(
        listOf(
            Geolocation(
                id = 1,
                status = "success",
                country = "United States",
                countryCode = "US",
                region = "MA",
                regionName = "Massachusetts",
                city = "Cambridge",
                zip = "02141",
                latitude = 42.3698,
                longitude = -71.0774,
                timezone = "America/New_York",
                internetServiceProvider = "Cloudflare London, LLC",
                organization = "HubSpot, Inc.",
                autonomousSystem = "AS209242 Cloudflare London, LLC",
                query = "199.60.103.11",
                timestamp = 1716066210610
            ),
            Geolocation(
                id = 2,
                status = "success",
                country = "United States",
                countryCode = "US",
                region = "NC",
                regionName = "North Carolina",
                city = "Durham",
                zip = "27703",
                latitude = 35.9806,
                longitude = -78.8426,
                timezone = "America/New_York",
                internetServiceProvider = "Windstream Communications LLC",
                organization = "Windstream Communications LLC",
                autonomousSystem = "AS7029 Windstream Communications LLC",
                query = "65.23.8.124",
                timestamp = 1719043980610
            )
        )
    )
}