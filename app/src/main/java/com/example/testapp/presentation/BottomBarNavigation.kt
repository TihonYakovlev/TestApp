package com.example.testapp.presentation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.example.testapp.BottomBarRoutes
import com.example.testapp.ScreenRoutes
import com.example.testapp.presentation.screens.DetailScreen
import com.example.testapp.presentation.screens.LocationScreen
import com.example.testapp.presentation.screens.ProfileScreen
import com.example.testapp.presentation.screens.SplashScreen
import com.example.testapp.presentation.screens.WeatherScreen
import com.example.testapp.viewmodels.WeatherViewModel

@Composable
fun BottomBarNavigation(
    navHostController: NavHostController,
    padding: PaddingValues,
    context: Activity,
    weatherViewModel: WeatherViewModel
) {
    NavHost(
        navController = navHostController, startDestination = ScreenRoutes.Splash.route,
        modifier = Modifier.padding(padding)
    ) {

        composable(ScreenRoutes.Splash.route) {
            SplashScreen(navHostController = navHostController)
        }
        navigation(
            route = ScreenRoutes.BottomBar.route,
            startDestination = BottomBarRoutes.CITY.routes
        ) {
            composable(BottomBarRoutes.CITY.routes) {
                WeatherScreen(
                    viewModel = weatherViewModel,
                    navHostController = navHostController,
                    context
                )
            }
            composable(BottomBarRoutes.COORDS.routes) {
                LocationScreen(viewModel = weatherViewModel ,navHostController = navHostController)
            }
            composable(BottomBarRoutes.SOME.routes) {
                ProfileScreen(navHostController = navHostController)
            }
        }
        composable(ScreenRoutes.Detail.route) {
            DetailScreen(navHostController = navHostController)
        }
    }

}

@Composable
fun BottomBarRow(
    navHostController: NavHostController
) {

    val tabList = listOf(
        BottomBarRoutes.CITY,
        BottomBarRoutes.COORDS,
        BottomBarRoutes.SOME
    )

    val navStackBackEntry by navHostController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabList.forEach { tab ->
            BottomBarItems(
                tab = tab,
                currentDestination = currentDestination,
                navHostController = navHostController
            )
        }
    }

}

@Composable
fun BottomBarItems(
    tab: BottomBarRoutes,
    currentDestination: NavDestination?,
    navHostController: NavHostController
) {

    val selected = currentDestination?.hierarchy?.any { it.route == tab.routes } == true

    val contentColor =
        if (selected) Color.Unspecified else MaterialTheme.colorScheme.onPrimary

    IconButton(onClick = {
        navHostController.navigate(tab.routes)
    }) {
        Icon(
            painter = painterResource(id = tab.icon),
            contentDescription = "",
            tint = contentColor,
            modifier = Modifier.size(30.dp)
        )
    }
}