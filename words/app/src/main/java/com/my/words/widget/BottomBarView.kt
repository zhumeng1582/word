package com.my.words.widget

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBarView(navCtrl: NavHostController) {
    val bottomNavList = listOf(
        BottomNavRoute.Home,
        BottomNavRoute.Book,
        BottomNavRoute.Collection,
        BottomNavRoute.Profile
    )
    NavigationBar(containerColor = MaterialTheme.colorScheme.background,contentColor = MaterialTheme.colorScheme.background) {
        val navBackStackEntry by navCtrl.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomNavList.forEach { bottomNav ->
            NavigationBarItem(
                icon = {
                    CustomIconButton(onClick = {
                        if (currentDestination?.route != bottomNav.routeName) {
                            navCtrl.navigate(bottomNav.routeName) {
                                popUpTo(navCtrl.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }) {
                        Icon(imageVector = bottomNav.icon, contentDescription = stringResource(bottomNav.stringId))
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.background,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                ),
                label = { Text(text = stringResource(bottomNav.stringId)) },
                selected = currentDestination?.hierarchy?.any { it.route == bottomNav.routeName } == true,
                onClick = {
                    if (currentDestination?.route != bottomNav.routeName) {
                        navCtrl.navigate(bottomNav.routeName) {
                            popUpTo(navCtrl.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                })
        }
    }
}
@Composable
fun CustomIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { onClick() })
        }
    ) { content() }
}