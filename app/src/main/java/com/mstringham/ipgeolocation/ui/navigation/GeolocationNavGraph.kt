package com.mstringham.ipgeolocation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mstringham.ipgeolocation.ui.history.HistoryDestination
import com.mstringham.ipgeolocation.ui.history.HistoryScreen
import com.mstringham.ipgeolocation.ui.history.HistoryViewModel
import com.mstringham.ipgeolocation.ui.home.HomeDestination
import com.mstringham.ipgeolocation.ui.home.HomeScreen
import com.mstringham.ipgeolocation.ui.home.HomeViewModel

/**
 * Main App
 * Provides Navigation graph for the application.
 */
@Composable
fun GeolocationApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            val viewModel: HomeViewModel = hiltViewModel()
            HomeScreen(
                issueIpGeolocationQuery = {
                    viewModel.issueIpGeolocationQuery()
                },
                onSearchQueryChange = {
                    viewModel.onSearchQueryChange(it)
                },
                navigateToHistoryScreen = {
                    navController.navigate(HistoryDestination.route)
                },
                state = viewModel.state,
                searchQuery = viewModel.searchQuery
            )
        }
        composable(route = HistoryDestination.route) {
            val viewModel: HistoryViewModel = hiltViewModel()
            HistoryScreen(
                navigateBack = { navController.navigateUp() },
                deleteHistory = { viewModel.deleteAllHistoryItems() },
                state = viewModel.state
            )
        }
    }
}
