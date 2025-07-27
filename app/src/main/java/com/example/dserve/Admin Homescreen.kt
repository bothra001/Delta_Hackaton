import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.dserve.RetrofitInstance
import com.example.dserve.StallRequest
import kotlinx.coroutines.launch
import com.example.dserve.MenuItemRequest

@Composable
fun admhome() {
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var stallName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var itemName by remember { mutableStateOf("") }
    var itemPrice by remember { mutableStateOf("") }
    val menuList = remember { mutableStateListOf<Pair<String, String>>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3F2FD))
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("DServe Stall Admin", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D47A1))

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = stallName,
            onValueChange = { stallName = it },
            label = { Text("Stall Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Stall Description") },
            singleLine = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Add Menu Item", fontSize = 20.sp, fontWeight = FontWeight.Medium)

        OutlinedTextField(
            value = itemName,
            onValueChange = { itemName = it },
            label = { Text("Item Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = itemPrice,
            onValueChange = { itemPrice = it },
            label = { Text("Item Price") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (itemName.isNotBlank() && itemPrice.isNotBlank()) {
                    menuList.add(itemName to itemPrice)
                    itemName = ""
                    itemPrice = ""
                }
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Add Menu Item")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Added Menu Items:", fontWeight = FontWeight.SemiBold)

        menuList.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${item.first} - ₹${item.second}")
                Button(onClick = { menuList.removeAt(index) }) {
                    Text("Remove")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            coroutineScope.launch {
                try {
                    val payload = StallRequest(
                        name = stallName,
                        description = description,
                        menu = menuList.map {
                            MenuItemRequest(it.first, it.second.toDoubleOrNull() ?: 0.0)
                        }
                    )
                    val response = RetrofitInstance.api.addStall(payload)
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Stall added successfully!", Toast.LENGTH_SHORT).show()
                        stallName = ""
                        description = ""
                        menuList.clear()
                    } else {
                        Log.e("AddStall", "Error: ${response.errorBody()?.string()}")
                    }
                } catch (e: Exception) {
                    Log.e("AddStall", "Exception: ${e.message}")
                }
            }
        }) {
            Text("Submit Stall")
        }
    }
}


