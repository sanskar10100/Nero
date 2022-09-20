package dev.sanskar.nero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.sanskar.nero.ui.AddBook
import dev.sanskar.nero.ui.HomeScreen
import dev.sanskar.nero.ui.StatsScreen
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun MainContent(modifier: Modifier = Modifier) {
        var bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        ModalBottomSheetLayout(
            modifier = modifier,
            sheetState = bottomSheetState,
            sheetElevation = 5.dp,
            sheetShape = RoundedCornerShape(16.dp),
            sheetContent = { AddBook() }
        ) {
            Scaffold(
                bottomBar = { BottomNav(navController = navController) },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.Center,
                floatingActionButton = {
                    FloatingActionButton(onClick = { scope.launch { bottomSheetState.show() } }) {
                        Icon(imageVector = Icons.Default.AddBox, contentDescription = null)
                    }
                }
            ) {
                NavHost(navController = navController, startDestination = Screen.Home.route) {
                    composable(Screen.Home.route) { HomeScreen() }
                    composable(Screen.Stats.route) { StatsScreen() }
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
            modifier = modifier,
            cutoutShape = CircleShape
        ) {
            BottomNavigation {
                BottomNavigationItem(
                    selected = currentScreenRoute == Screen.Home.route,
                    onClick = { if (currentScreenRoute != Screen.Home.route) navController.navigate(Screen.Home.route) },
                    icon = { Icon(Screen.Home.icon, contentDescription = null) }
                )
                BottomNavigationItem(
                    selected = currentScreenRoute == Screen.Stats.route,
                    onClick = { if (currentScreenRoute != Screen.Stats.route) navController.navigate(Screen.Stats.route) },
                    icon = { Icon(Screen.Stats.icon, contentDescription = null) }
                )
            }
        }
    }
}