package io.b306.picashow

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun SetupNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "firstPage") {
        composable("firstPage") { FirstPage() }
        composable("secondPage") { SecondPage() }
        composable("thirdPage") { ThirdPage() }
    }
}