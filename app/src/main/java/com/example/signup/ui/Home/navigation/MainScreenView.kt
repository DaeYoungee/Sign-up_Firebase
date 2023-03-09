package com.example.signup.ui.Home.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.signup.R
import com.example.signup.ui.Home.navigation.screen.HomeScreen
import com.example.signup.ui.Home.navigation.screen.SettingScreen
import com.example.signup.ui.Home.navigation.screen.TimelineScreen

sealed class BottomNavItem(val screenRoute: String, val icon: Int, val title: Int) {
    object FirstScreen :
        BottomNavItem(screenRoute = FIRST, icon = R.drawable.baseline_dashboard_24, title = R.string.text_board)

    object SecondScreen :
        BottomNavItem(screenRoute = SECOND, icon = R.drawable.baseline_timeline_24, title = R.string.text_timeline)

    object ThirdScreen :
        BottomNavItem(screenRoute = THIRD, icon = R.drawable.baseline_settings_24, title = R.string.text_settings)
}

@Composable
fun MainScreenView(logout: () -> Unit, saveStore: (String) -> Unit) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigation(navController = navController)}
    ) {
        Box(modifier = Modifier.padding(it)) {
            NavigationGraph(navController = navController, logout = logout, saveStore = saveStore)
        }
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    logout: () -> Unit,
    saveStore: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.FirstScreen.screenRoute
    ) {
        composable(route = BottomNavItem.FirstScreen.screenRoute) {
            HomeScreen(logout = { logout() }, saveStore = saveStore)
        }
        composable(route = BottomNavItem.SecondScreen.screenRoute) {
            TimelineScreen()
        }
        composable(route = BottomNavItem.ThirdScreen.screenRoute) {
            SettingScreen()
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf<BottomNavItem>(
        BottomNavItem.FirstScreen,
        BottomNavItem.SecondScreen,
        BottomNavItem.ThirdScreen
    )

    androidx.compose.material.BottomNavigation(backgroundColor = Color.White, contentColor = Color(0xFF3F414E)) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = stringResource( id = item.title,),
                        modifier = Modifier.size(26.dp)
                    )
                },
                label = { Text(text = stringResource(id = item.title), fontSize = 9.sp)},
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                selected = item.screenRoute == currentRoute,
                alwaysShowLabel = false,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) {saveState = true}
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })

        }

    }
}
