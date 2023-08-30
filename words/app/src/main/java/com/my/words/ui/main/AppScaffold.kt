package com.my.words.ui.main

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
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
import com.my.words.ui.books.WordListViewModel
import com.my.words.ui.select.SelectWordBookPage
import com.my.words.ui.webview.WebViewPage
import com.my.words.ui.word.WordDetailPage
import com.my.words.ui.word.WordViewModel
import com.my.words.widget.BottomNavBarView
import com.my.words.widget.RouteName

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AppScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding(),
        bottomBar = {
            when (currentDestination?.route) {
                RouteName.HOME -> BottomNavBarView(navCtrl = navController)
                RouteName.BOOK -> BottomNavBarView(navCtrl = navController)
                RouteName.COLLECTION -> BottomNavBarView(navCtrl = navController)
                RouteName.PROFILE -> BottomNavBarView(navCtrl = navController)
            }
        }
    ) {
        NavHost(navController = navController, startDestination = RouteName.HOME) {
            composable(RouteName.HOME) { HomePage(navController) }
            composable(RouteName.BOOK) { BooksPage(navController) }
            composable(RouteName.COLLECTION) { WebViewPage(navController, url = "https://web.shanbay.com/bdc/vocabtest#/") }
            composable(RouteName.PROFILE) { SelectWordBookPage(navController) }
            composable(RouteName.SELECT_WORD) { SelectWordBookPage(navController) }
            composable(RouteName.DETAIL) {
                it.arguments?.getString("index")
                    ?.let { index ->
                        val wordViewModel: WordViewModel = viewModel()
                        when (index) {
                            "LEARNT" ,"ERROR","DONE","ALL" -> {
                                wordViewModel.setList(index)
                            }
                            else -> {
                                wordViewModel.setAssetName(index.toInt())
                            }
                        }

                        WordDetailPage(navController)
                    }
            }
            composable(RouteName.WordListPage) {
                it.arguments?.getString("type")
                    ?.let { type ->
                        val vm: WordListViewModel = viewModel()
                        vm.setListType(type)
                        WordListPage(navController)
                    }
            }
        }

    }
}
