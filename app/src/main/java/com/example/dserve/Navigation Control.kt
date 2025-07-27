package com.example.dserve

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.dserve.ui.theme.admlogin

@Composable
fun AppNavigator(navcontroller: NavHostController){
    NavHost(startDestination = "home" , navController = navcontroller ){
        composable ("home" ){ HomeScreen(navcontroller)}
        composable("student signup"){Stusp(navcontroller)}
        composable("adm signup"){Admsp(navcontroller)}
        composable("adm login"){ admlogin(navcontroller) }
        composable("student login"){stulogin(navcontroller)}
    }
}



