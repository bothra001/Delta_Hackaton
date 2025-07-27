package com.example.dserve

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController

@Composable
fun HomeScreen(navcontroller: NavController){
    Box(modifier =Modifier.fillMaxWidth()
        .fillMaxHeight(0.5f)){
        Image(
            painterResource()
        )
    }
}