package dev.sanskar.nero.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddBox
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.sanskar.nero.ui.add.AddScreen
import dev.sanskar.nero.ui.add.AddViewModel
import dev.sanskar.nero.ui.detail.DetailScreen
import dev.sanskar.nero.ui.home.HomeScreen
import dev.sanskar.nero.ui.stats.StatsScreen
import dev.sanskar.nero.ui.theme.NeroTheme
import dev.sanskar.nero.util.Screen
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NeroTheme {
                MainContent()
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
    @Composable
    fun MainContent(
        modifier: Modifier = Modifier,
    ) {
        val addViewModel: AddViewModel = hiltViewModel() // Needed for clearing bottom sheet state
        val keyboardController = LocalSoftwareKeyboardController.current
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

        BackHandler(bottomSheetState.targetValue == ModalBottomSheetValue.Expanded) {
            scope.launch {
                bottomSheetState.hide()
                keyboardController?.hide()
            }
        }

        LaunchedEffect(bottomSheetState.targetValue) {
            if (bottomSheetState.targetValue == ModalBottomSheetValue.Hidden) {
                addViewModel.clearSearchState()
                keyboardController?.hide()
            } else {
                keyboardController?.show()
            }
        }

        ModalBottomSheetLayout(
            modifier = modifier,
            sheetState = bottomSheetState,
            sheetElevation = 5.dp,
            sheetShape = RoundedCornerShape(16.dp),
            sheetContent = { AddScreen(viewModel = addViewModel) { scope.launch { bottomSheetState.hide() } } }
        ) {
            Scaffold(
                bottomBar = { BottomNav(navController = navController) },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(
                        backgroundColor = MaterialTheme.colors.primary,
                        onClick = {
                        scope.launch {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddBox,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onPrimary,
                        )
                    }
                }
            ) { padding ->
                NavHost(navController = navController, startDestination = Screen.Home.route) {

                    composable(Screen.Home.route) {
                        HomeScreen(
                            modifier = Modifier.padding(padding),
                            viewModel = hiltViewModel(it)
                        ) {
                            navController.navigate("${Screen.BookDetail.route}/${it}")
                        }
                    }

                    composable(Screen.Stats.route) {
                        StatsScreen(
                            modifier = Modifier.padding(padding),
                            viewModel = hiltViewModel(it)
                        )
                    }

                    composable(
                        "${Screen.BookDetail.route}/{book_id}",
                        arguments = listOf(navArgument("book_id") { type = NavType.StringType })
                    ) {
                        DetailScreen(
                            bookId = it.arguments?.getString("book_id") ?: "",
                            modifier = Modifier.padding(padding),
                            viewModel = hiltViewModel(it)
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNav(navController: NavController, modifier: Modifier = Modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentScreenRoute by remember {
            derivedStateOf { navBackStackEntry?.destination?.route }
        }
        BottomAppBar(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 32f, topEnd = 32f)),
            cutoutShape = CircleShape
        ) {
            BottomNavigation {
                BottomNavigationItem(
                    selected = currentScreenRoute == Screen.Home.route,
                    onClick = { if (currentScreenRoute != Screen.Home.route) navController.popBackStack() },
                    icon = { Icon(Screen.Home.icon!!, contentDescription = null) }
                )
                BottomNavigationItem(
                    selected = currentScreenRoute == Screen.Stats.route,
                    onClick = {
                        if (currentScreenRoute != Screen.Stats.route) navController.navigate(
                            Screen.Stats.route
                        )
                    },
                    icon = { Icon(Screen.Stats.icon!!, contentDescription = null) }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavPreview() {
        NeroTheme {
            BottomNav(rememberNavController())
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainContentPreview() {
        NeroTheme {
            MainContent()
        }
    }
}