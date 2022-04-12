package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.Conversation
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private val conversations = database.child("conversations")

    private val _listOfMessages = MutableLiveData<List<Conversation>>()
    val listOfMessages : LiveData<List<Conversation>>
    get() = _listOfMessages

    private val users = database.child("users")
    private var auth: FirebaseAuth = Firebase.auth

    var  userReceiver: User? = null




/*
postReference.addValueEventListener(postListener)

     */


    fun getUser(key: String) {

        users.orderByKey().equalTo(key).get().addOnSuccessListener {

            it.children.forEach {
                userReceiver = User(
                    name = it.child("name").value.toString(),
                    mail = it.child("mail").value.toString(),
                    age = it.child("age").value.toString().toInt(),
                    key = it.key.toString()
                )

            }
        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }.addOnCompleteListener {
            Log.d("firebase", "En el chat ViewModel complete listener ${it.result}")
        }
    }




    fun getConversation() {
        val listOfMessages = mutableListOf<Conversation>()

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("fare", "El snapshot trae ${snapshot}")
                snapshot.children.forEach {

                    val messages = Conversation(
                        keySender = it.child("keySender").value.toString(),
                        keyReceiver = it.child("keyReceiver").value.toString(),
                        content = it.child("content").value.toString()
                    )


                        listOfMessages.add(messages)



                    Log.d("firebase", "Los hijos de snapshot $messages")

                }
                _listOfMessages.value = listOfMessages

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled")
            }

        }
        conversations.addValueEventListener(listener)
    }

    fun createMessage(keyReceiver:String?, content:String){
        val message = Conversation(keyReceiver = keyReceiver.toString(), keySender = "", content = content)
    conversations.push().setValue(message)
    }

}