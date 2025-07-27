package com.example.dserve

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigator(navcontroller: NavHostController){
    NavHost(startDestination = "home" , navController = navcontroller ){
        composable ("home" ){ HomeScreen(navcontroller)}
        composable("student signup"){Stusp(navcontroller)}
        composable("Admin signup"){Admsp(navcontroller)}
        composable("student login"){Stulgn(navcontroller)}
        composable("Admin Login"){AdmLgn(navcontroller)}
    }
}