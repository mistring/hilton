package com.mstringham.ipgeolocation.ui.home


import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mstringham.ipgeolocation.R
import com.mstringham.ipgeolocation.data.db.Geolocation
import com.mstringham.ipgeolocation.ui.GeolocationTopAppBar
import com.mstringham.ipgeolocation.ui.navigation.NavigationDestination
import com.mstringham.ipgeolocation.ui.theme.DeepBlue
import com.mstringham.ipgeolocation.ui.theme.IPGeolocationTheme
import com.mstringham.ipgeolocation.ui.theme.LightGray
import com.mstringham.ipgeolocation.ui.theme.TitleTextColor
import timber.log.Timber

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_screen_title
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    issueIpGeolocationQuery: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    navigateToHistoryScreen: () -> Unit,
    state: HomeState,
    searchQuery: String = ""
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            GeolocationTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(bottom = 48.dp)
        ) {
            HiltonHeader(modifier = Modifier.padding(innerPadding))

            IpGeoSearch(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    onSearchQueryChange(it)
                },
                onSearchEvent = {
                    issueIpGeolocationQuery()
                }
            )

            when (state) {
                is HomeState.Loading,
                is HomeState.Network -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Center,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }

                is HomeState.Result -> {
                    Text(
                        text = "Search issued at: ${DateFormat.format("HH:mm:ss a", System.currentTimeMillis())}",
                        modifier = Modifier.padding(horizontal = 16.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                    GeolocationItem(
                        source = state.source,
                        item = state.currentResult
                    )
                }

                is HomeState.Error -> {
                    Text(
                        text = "${state.error?.asString()}",
                        modifier = Modifier.padding(all = 16.dp)
                    )
                }

                else -> {
                    Timber.d("Home State: Idle")
                }
            }

            HistoryButton {
                navigateToHistoryScreen()
            }
        }

    }
}

@Composable
fun HiltonHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DeepBlue)
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(width = 2.dp, color = Color.White)
                .padding(vertical = 12.dp)
                .width(120.dp)
        ) {
            Text(
                text = stringResource(R.string.ip).uppercase(),
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                modifier = Modifier.align(Center),
                color = TitleTextColor
            )
        }
        Text(
            text = stringResource(R.string.geolocation).uppercase(),
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth(),
            fontFamily = FontFamily.SansSerif,
            fontSize = 16.sp,
            color = TitleTextColor,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HiltonHeaderPreview() {
    HiltonHeader(Modifier)
}

@Composable
fun IpGeoSearch(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchEvent: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                onSearchQueryChange(it)
            },
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text
            ),
            label = { Text(stringResource(R.string.ip_address_or_blank)) },
            singleLine = true
        )
        Button(
            onClick = {
                focusManager.clearFocus() // Close the keyboard when Search button is clicked
                onSearchEvent()
            },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBlue)
        ) {
            Text(stringResource(R.string.search_btn_label))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IpGeoSearchPreview() {
    IpGeoSearch("19.23.43.23", {}, {})
}

@Composable
private fun GeolocationItem(
    source: QuerySource,
    item: Geolocation,
) {
    Card(
        modifier = Modifier.padding(all = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .background(LightGray)
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_large)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        ) {
            // Local or Remote:
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "(${source.name})")
                Spacer(Modifier.weight(1f))
                Text(
                    text = item.formatTimestamp(),
                    fontStyle = FontStyle.Italic
                )
            }

            // Query
            Text(
                text = item.query,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 12.dp),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Text(
                text = "${item.city}, ${item.regionName} (${item.region}) ${item.zip}",
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${item.country} (${item.countryCode})",
                style = MaterialTheme.typography.titleMedium
            )

            // Lat/Lon
            Text(
                text = stringResource(R.string.latitude, item.latitude.toFloat()),
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.longitude, item.longitude.toFloat()),
                modifier = Modifier.align(CenterHorizontally),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            // Timezone
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.timezone),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.weight(1f))
                Text(text = item.timezone)
            }

            // ISP
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.isp),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = item.internetServiceProvider)
            }

            // ORG
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.org),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = item.organization)
            }

            // AS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = stringResource(R.string.`as`),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = item.autonomousSystem)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemoteGeolocationItemPreview() {
    IPGeolocationTheme {
        GeolocationItem(
            QuerySource.REMOTE,
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
                query = "199.60.103.11"
            )
        )
    }
}

@Composable
fun HistoryButton(
    navigateToHistoryScreen: () -> Unit
) {
    OutlinedButton(
        onClick = {
            navigateToHistoryScreen()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.history_button_label)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryButtonPreview() {
    HistoryButton() { }
}
