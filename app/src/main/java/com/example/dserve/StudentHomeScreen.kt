package com.example.dserve

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import org.json.JSONObject
import com.razorpay.Checkout

@Composable
fun stuhome(){
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()
        var stallList by remember { mutableStateOf<List<StallRequest>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            coroutineScope.launch {
                try {
                    stallList = RetrofitInstance.api.getStalls()
                } catch (e: Exception) {
                    Toast.makeText(context, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                } finally {
                    isLoading = false
                }
            }
        }

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(stallList) { stall ->
                    StallCard(stall)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
@Composable
fun StallCard(stall: StallRequest) {
    val activity = LocalContext.current

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stall.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = stall.description, fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            stall.menu.forEach { item ->
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable {
                        val checkout = Checkout()
                        checkout.setKeyID("rzp_test_ABC123") //

                        val options = JSONObject().apply {
                            put("name", stall.name)
                            put("description", item.itemName)
                            put("currency", "INR")
                            put("amount", (item.price * 100).toInt())
                        }

                        try {
                            checkout.open(activity as Activity?, options)
                        } catch (e: Exception) {
                            Toast.makeText(activity, "Payment error: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }


                ) {
                    Text(text = "${item.itemName} - ₹${item.price}", fontSize = 14.sp)
                }
            }
        }
    }
}






