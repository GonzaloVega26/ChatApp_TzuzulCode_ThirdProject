package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.Conversation
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.User
import com.gonzalo.chatapp_tzuzulcode_thirdproject.ui.theme.ChatApp_TzuzulCode_ThirdProjectTheme

class ChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        var key = ""
        if (bundle != null) {
            Log.d("firebase", "La key del Bundle ${bundle.getString("key")}")
            key = bundle.getString("key").toString()
        }
        val viewModel = ChatViewModel()
        viewModel.getUser(key)

        viewModel.getConversation()

        viewModel.listOfMessages.observe(this, Observer {
            setContent {
                ChatApp_TzuzulCode_ThirdProjectTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Column {
                            ShowChat(it, viewModel.userReceiver){
                                keyReceiver, content -> viewModel.createMessage(keyReceiver,content)
                            }

                        }

                    }
                }
            }
        })

    }
}

@Composable
fun ShowChat(conversations: List<Conversation>, userReceiver: User?,
             createMessage: (keyReceiver:String, content:String)->Unit) {
    Log.d("firebase", "En el chat ${userReceiver}")
    var message = remember { mutableStateOf("") }
    Column{
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(conversations) { conversation ->
                if(conversation.keyReceiver == userReceiver?.key){
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = conversation.content)
                    }
                }else{
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = conversation.content)
                    }
                }


            }
            item {
                Row(verticalAlignment = Alignment.Bottom) {

                    TextField(
                        value = message.value,
                        onValueChange = { message.value = it },
                        label = { Text("Message") },
                        modifier = Modifier.padding(5.dp)
                    )

                    Button(onClick = {
                        createMessage(userReceiver!!.key, message.value)
                    },
                        modifier = Modifier.padding(5.dp)) {
                        Text(text = "Send")
                    }
                }
            }
        }


    }


}