package com.my.words.widget

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.my.words.R

sealed class BottomNavRoute(
    var routeName: String,
    @StringRes var stringId: Int,
    var icon: ImageVector
) {
    object Home: BottomNavRoute(RouteName.HOME, R.string.home, Icons.Default.Home)
    object Book: BottomNavRoute(RouteName.BOOK, R.string.category, Icons.Default.Menu)
    object Collection: BottomNavRoute(RouteName.COLLECTION, R.string.collection, Icons.Default.Favorite)
    object Profile: BottomNavRoute(RouteName.PROFILE, R.string.profile, Icons.Default.Person)
}