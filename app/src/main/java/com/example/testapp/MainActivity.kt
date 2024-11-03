package com.example.testapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.testapp.ui.theme.TestAppTheme
import com.example.testapp.viewmodels.WeatherViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val weatherViewModel: WeatherViewModel by viewModels()
            TestAppTheme {
                val appState = rememberAppState()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (appState.shouldShowBottomBar)
                                BottomAppBar(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    modifier = Modifier
                                        .height(70.dp)
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 24.dp, topEnd = 24.dp
                                            )
                                        )
                                ) {
                                    BottomBarRow(
                                        navHostController = appState.navHostController,
                                    )
                                }
                        }
                    ) { innerPadding ->
                        BottomBarNavigation(
                            navHostController = appState.navHostController,
                            padding = innerPadding,
                            this,
                            weatherViewModel
                        )
                    }
                }
                // WeatherScreen(weatherViewModel)
            }
        }
    }
}

enum class BottomBarRoutes(
    val id: Int,
    @StringRes val title: Int,
    val routes: String,
    @DrawableRes val icon: Int
) {
    CITY(1, R.string.city_bar, "/city", R.drawable.city),
    COORDS(
        2,
        R.string.coords_bar, "/coordinates", R.drawable.coords
    ),
    SOME(3, R.string.some_bar, "/some", R.drawable.some)

}

sealed class ScreenRoutes(val route: String) {
    data object Splash : ScreenRoutes("/splash")
    data object BottomBar : ScreenRoutes("/bottombar")
    data object Detail : ScreenRoutes("/detail")
}


@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel, navHostController: NavHostController,
    context: Activity
) {
    val weather by viewModel.weather
    val city = remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Box(contentAlignment = Alignment.Center) {
            IconButton(onClick = {
                navHostController.navigate(ScreenRoutes.Detail.route)
            }) {
                Icon(
                    Icons.Filled.Settings,
                    contentDescription = "Details", modifier = Modifier.size(80.dp),
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = city.value,
                onValueChange = { newText -> city.value = newText },
                singleLine = true
            )

            Button(onClick = { viewModel.fetchWeather(city.value) }) {
                Text(text = "Get Weather")
            }

            Spacer(modifier = Modifier.height(16.dp))

            weather?.let {
                Text(text = "City: ${it.name}")
                Text(text = "Temperature: ${it.main.temp.toInt()}°C")
                Text(text = "Feels Like: ${it.main.feels_like.toInt()}°C")
            }

        }



        BackHandler {
            context.finish()
        }
    }
}

@Composable
fun SplashScreen(
    navHostController: NavHostController
) {

    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navHostController.navigate(ScreenRoutes.BottomBar.route)
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Splash Screen", style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        )
    }
}

@Composable
fun HomeScreen(
    navHostController: NavHostController,
    context: Activity
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            navHostController.navigate(ScreenRoutes.Detail.route)
        }) {
            Text(text = "Move to details screen")
        }
    }

    BackHandler {
        context.finish()
    }

}

@Composable
fun NotificationScreen(
    navHostController: NavHostController
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Notification Screen", style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        )
    }
    BackHandler {
        navHostController.navigate(BottomBarRoutes.CITY.routes) {
            popUpTo(BottomBarRoutes.CITY.routes) {
                inclusive = true
            }
        }
    }
}


@Composable
fun ProfileScreen(
    navHostController: NavHostController
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Profile Screen", style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        )
    }

    BackHandler {
        navHostController.navigate(BottomBarRoutes.CITY.routes) {
            popUpTo(BottomBarRoutes.CITY.routes) {
                inclusive = true
            }
        }
    }

}

@Composable
fun DetailScreen(
    navHostController: NavHostController
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "Detail Screen", style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 30.sp
            )
        )
    }

}

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
                NotificationScreen(navHostController = navHostController)
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

@Composable
fun rememberAppState(
    navHostController: NavHostController = rememberNavController()
) = remember(navHostController) {
    AppState(navHostController)
}

@Stable
class AppState(
    val navHostController: NavHostController
) {

    private val routes = BottomBarRoutes.values().map { it.routes }

    val shouldShowBottomBar: Boolean
        @Composable get() =
            navHostController.currentBackStackEntryAsState().value?.destination?.route in routes
}

