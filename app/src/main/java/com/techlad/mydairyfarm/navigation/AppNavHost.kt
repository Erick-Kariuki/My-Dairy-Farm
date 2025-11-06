package com.techlad.mydairyfarm.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techlad.mydairyfarm.ui.theme.screens.cows.AddNewCowScreen
import com.techlad.mydairyfarm.ui.theme.screens.cows.ViewCowsScreen
import com.techlad.mydairyfarm.ui.theme.screens.dashboard.DashboardScreen
import com.techlad.mydairyfarm.ui.theme.screens.login.ForgotPasswordScreen
import com.techlad.mydairyfarm.ui.theme.screens.login.LoginScreen
import com.techlad.mydairyfarm.ui.theme.screens.register.RegisterScreen

@Composable
fun AppNavHost(navController: NavHostController = rememberNavController(),
               startDestination: String = ROUTE_DASHBOARD,
               modifier: Modifier = Modifier
){
    NavHost(navController = navController, startDestination = startDestination){
        composable(ROUTE_REGISTER){ RegisterScreen(navController) }
        composable(ROUTE_DASHBOARD){ DashboardScreen(navController) }
        composable(ROUTE_LOGIN){ LoginScreen(navController) }
        composable(ROUTE_FORGOT_PASSWORD){ ForgotPasswordScreen(navController) }
        composable (ROUTE_ADD_NEW_COW){ AddNewCowScreen(navController) }
        composable (route = "$ROUTE_VIEW_COW/{status}",
            arguments = listOf(navArgument("status"){type = NavType.StringType})
        ){ backStackEntry ->
            val status = backStackEntry.arguments?.getString("status") ?: ""
            ViewCowsScreen(navController, status)
        }

    }
}