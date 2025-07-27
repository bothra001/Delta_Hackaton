package com.example.dserve

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.dserve.ui.theme.DserveTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DserveTheme {
                val navController= rememberNavController()
                AppNavigator(navController)
                fun onPaymentSuccess(razorpayPaymentID: String?) {
                    Toast.makeText(this, "Payment Success: $razorpayPaymentID", Toast.LENGTH_SHORT).show()
                }

                fun onPaymentError(code: Int, response: String?) {
                    Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}