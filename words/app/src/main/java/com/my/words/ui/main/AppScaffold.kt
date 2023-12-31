package com.my.words.ui.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.my.words.ui.books.BooksPage
import com.my.words.ui.books.WordListPage
import com.my.words.ui.profile.Profile
import com.my.words.ui.profile.Setting
import com.my.words.ui.record.PunchCardCalendar
import com.my.words.ui.select.SelectWordBookPage
import com.my.words.ui.webview.WebViewPage
import com.my.words.ui.word.WordDetailPage
import com.my.words.ui.word.WordTestPage
import com.my.words.ui.word.WordTestViewModel
import com.my.words.ui.word.WordViewModel
import com.my.words.widget.BottomNavBarView
import com.my.words.widget.RouteName

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val vm: WordViewModel = viewModel()

    Scaffold(
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.HOME -> BottomNavBarView(navCtrl = navController)
                RouteName.BOOK -> BottomNavBarView(navCtrl = navController)
                RouteName.COLLECTION -> BottomNavBarView(navCtrl = navController)
                RouteName.PROFILE -> BottomNavBarView(navCtrl = navController)
            }
        }
    ) { paddingValues ->
        NavHost(navController = navController, startDestination = RouteName.HOME,modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            composable(RouteName.HOME) { HomePage(navController) }
            composable(RouteName.BOOK) { BooksPage(navController) }
            composable(RouteName.COLLECTION) {
                WebViewPage(
                    navController,
                    url = "https://shared.youdao.com/dict/market/recite-vocabulary/dist/index.html"
                )
            }
            composable(RouteName.PROFILE) { Profile(navController) }
            composable(RouteName.SETTING) { Setting(navController) }
            composable(RouteName.SELECT_WORD) { SelectWordBookPage(navController) }
            composable(RouteName.LEARN_PAGE_TYPE_INDEX) {
                it.arguments?.getString("type")
                    ?.let { type ->
                        vm.setListType(type)
                        WordDetailPage(
                            navController,
                            it.arguments?.getString("type") ?: "5",
                            it.arguments?.getString("index")?.toInt() ?: 0,
                            viewModel = vm
                        )
                    }

            }
            composable(RouteName.DETAIL_INDEX) {
                WordDetailPage(
                    navController,
                    it.arguments?.getString("type") ?: "5",
                    it.arguments?.getString("index")?.toInt() ?: 0,
                    viewModel = vm
                )
            }
            composable(RouteName.WORD_TEST) {
                val wordTestViewModel = WordTestViewModel(vm.beanList.value!!)
                WordTestPage(
                    navController,
                    it.arguments?.getString("type") ?: "5",
                    wordTestViewModel
                )
            }
            composable(RouteName.WORD_BOOK_TEST) {
                val wordTestViewModel = WordTestViewModel(vm.beanList.value!!)
                WordTestPage(
                    navController,
                    it.arguments?.getString("type") ?: "5",
                    wordTestViewModel
                )
            }
            composable(RouteName.RecordCalendar) {
                PunchCardCalendar()
            }
            composable(RouteName.WordListPage) {
                it.arguments?.getString("type")
                    ?.let { type ->
                        vm.setListType(type)
                        WordListPage(navController, viewModel = vm)
                    }
            }
        }

    }
}
