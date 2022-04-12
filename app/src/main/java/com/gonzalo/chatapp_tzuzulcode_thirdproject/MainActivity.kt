package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.User
import com.gonzalo.chatapp_tzuzulcode_thirdproject.ui.theme.ChatApp_TzuzulCode_ThirdProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    // Firebase Auth instance
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = HomeViewModel()
        // Initialize Firebase Auth and check if the user is signed in
        auth = Firebase.auth
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }


        viewModel.isLoggedIn.observe(this, Observer {
            if (!it) {
                this.finish()
            }
        })

        viewModel.navigateToChat.observe(this, Observer {
            if (it) {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("key", viewModel.keyOfChat.value)

                startActivity(intent)

            }
        })
        viewModel.getAllUsers()
        viewModel.tengoDatos.observe(this, Observer {
            if (it){
                setContent {
                    ChatApp_TzuzulCode_ThirdProjectTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.background
                        ) {
                            Column {
                                Greeting("Hello")

                                LogOut {
                                    viewModel.logout()
                                }

                                ShowUsers(viewModel.listOfUsers.value!!
                                ) { email -> viewModel.navigateToChat(email) }

                                Button(onClick = { viewModel.writeInfoToDatabase() }) {
                                    Text(text = "Write")
                                }
                            }

                        }
                    }
                }
            }
        })





    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in.
        if (auth.currentUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }
    }
}


@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun LogOut(logout: () -> Unit) {
    Column {
        Button(onClick = { logout() }) {
            Text(text = "Log Out")
        }
    }
}

@Composable
fun ShowUsers(users : List<User>,
navigateToChat: (mail:String)-> Unit) {


    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        items(users) { user ->
            Row() {
                Text(user.name)
                Spacer(modifier = Modifier.padding(10.dp))
                Text(user.mail)
                Button(onClick = {
                    Log.d("firebase", "La key que se manda es ${user.mail}")
                    navigateToChat(user.key) }) {
                    Text("Chat")
                }


            }

        }
    }
}
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatApp_TzuzulCode_ThirdProjectTheme {
        Greeting("Android")
    }
}