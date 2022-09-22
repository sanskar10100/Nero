package dev.sanskar.nero.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.runtime.saveable.LocalSaveableStateRegistry
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.findNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.sanskar.nero.ui.AddBook
import dev.sanskar.nero.ui.HomeScreen
import dev.sanskar.nero.ui.StatsScreen
import dev.sanskar.nero.ui.theme.NeroTheme
import dev.sanskar.nero.util.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

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
        viewModel: MainViewModel = hiltViewModel(),
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()

        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
        )

        LaunchedEffect(bottomSheetState.targetValue) {
            if (bottomSheetState.targetValue == ModalBottomSheetValue.Hidden) {
                viewModel.clearSearchState()
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
            sheetContent = { AddBook { scope.launch { bottomSheetState.hide() } } }
        ) {
            Scaffold(
                bottomBar = { BottomNav(navController = navController) },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        scope.launch {
                            bottomSheetState.animateTo(ModalBottomSheetValue.Expanded)
                        }
                    }
                    ) {
                        Icon(imageVector = Icons.Default.AddBox, contentDescription = null)
                    }
                }
            ) { padding ->
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) { HomeScreen(modifier = Modifier.padding(padding)) {
                        navController.navigate("${Screen.BookDetail.route}/${it}")
                    } }
                    composable(Screen.Stats.route) { StatsScreen(modifier = Modifier.padding(padding)) }
                    composable(
                        "${Screen.BookDetail.route}/{book_id}",
                        arguments = listOf(navArgument("book_id") { type = NavType.StringType })
                    ) {
                        BookDetail(bookId = it.arguments?.getString("book_id") ?: "")
                    }
                }
            }
        }
    }

    @Composable
    fun BottomNav(navController: NavController,  modifier: Modifier = Modifier) {
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
                    onClick = { if (currentScreenRoute != Screen.Stats.route) navController.navigate(Screen.Stats.route) },
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