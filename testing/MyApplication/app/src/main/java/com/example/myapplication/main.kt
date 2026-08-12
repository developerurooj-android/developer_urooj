package com.example.myapplication

import java.lang.reflect.Modifier

@Composable
fun LoginScreen() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Title
        Text(
            text = "Login",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Username field
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Password field
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Login button
        Button(onClick = {
            message = if (username == "admin" && password == "1234") {
                "Login Successful!"
            } else {
                "Invalid credentials"
            }
        }) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Message
        Text(text = message, color = Color.Red)
    }
}