package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.gonzalo.chatapp_tzuzulcode_thirdproject.ui.theme.ChatApp_TzuzulCode_ThirdProjectTheme

class LoginActivity : ComponentActivity() {
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

        viewModel.isRegistered.observe(this, Observer {
            if (it) {
                val intent = Intent(this, RegisterActivity::class.java)
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
                    Column {
                        Login { email, password ->
                            viewModel.logIn(email = email, password = password)
                        }
                        CreateAccount {
                            viewModel.navigateToRegister()
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun Login(
    auth: (email: String, password: String) -> Unit
) {
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            value = mail,
            onValueChange = { mail = it },
            label = { Text("Email") },
            modifier = Modifier.padding(5.dp)
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("PassWord") },
            modifier = Modifier.padding(5.dp)

        )

        Button(onClick = { auth(mail, password) }, modifier = Modifier.fillMaxWidth(0.5f)) {
            Text(text = "LogIn")
        }
    }

}

@Composable
fun CreateAccount(register: () -> Unit) {
    Column {
        Text(text = "New to the App? Register here!", modifier = Modifier.clickable {
            register()
        })
    }
}