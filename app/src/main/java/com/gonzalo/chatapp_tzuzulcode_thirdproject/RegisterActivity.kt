package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.gonzalo.chatapp_tzuzulcode_thirdproject.ui.theme.ChatApp_TzuzulCode_ThirdProjectTheme

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Instanciate the ViewModel
        val viewModel = AuthViewModel()

        //Observing if the user is logged In, so when it does it will navigate to MainActivity
        viewModel.isLoggedIn.observe(this, Observer {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        })

        setContent {
            ChatApp_TzuzulCode_ThirdProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Register(
                        { email, password ->
                        viewModel.createAccount(email = email, password = password)
                        },

                        { email, age, name ->
                            viewModel.createUser(name = name, age = age, email = email)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Register(
    auth: (email: String, password: String) -> Unit,
    createAccount: (email: String, age: Int, name: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    var age by remember { mutableStateOf(0) }
    var name by remember { mutableStateOf("") }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(0.8f)
    ) {
        Image(
            painter = painterResource(R.drawable.login_bg), contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(1f)
                .height(200.dp)
                .padding(5.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.padding(5.dp)
        )

        TextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            )
        )

        OutlinedTextField(
            value = age.toString(),
            onValueChange = { age = it.toInt() },
            label = { Text("Age") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.padding(5.dp)

        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.padding(5.dp)

        )

        Button(
            onClick = {
                auth(email, password.value)
                createAccount(email, age, name)
            },

            modifier = Modifier.fillMaxWidth(0.5f)
        ) {
            Text(text = "Register")
        }
    }

}