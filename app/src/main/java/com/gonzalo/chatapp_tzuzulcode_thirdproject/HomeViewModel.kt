package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private val TAG = "HOME"

    private var auth: FirebaseAuth = Firebase.auth

    private val _isLoggedIn = MutableLiveData(true)
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private var _listOfUsers = MutableLiveData<List<User>>(listOf())
    val listOfUsers: LiveData<List<User>>
        get() = _listOfUsers

    private var _tengoDatos = MutableLiveData(false)
    val tengoDatos: LiveData<Boolean>
        get() = _tengoDatos

    private var _navigateToChat = MutableLiveData(false)
    val navigateToChat :LiveData<Boolean>
        get() = _navigateToChat

    private var _keyOfChat = MutableLiveData("")
    val keyOfChat :LiveData<String>
        get() = _keyOfChat

    fun logout() {
        auth.signOut()
        _isLoggedIn.value = false
    }

    fun navigateToChat(key:String){
        _keyOfChat.value = key
        _navigateToChat.value = true
        Log.d("firebase","La key que llega es $key")

    }
    fun navigatedToChat(){
        _keyOfChat.value = ""
        _navigateToChat.value = false


    }

    fun getUserInfo() {
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email


            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    fun getAllUsers() {
        val listOfUsers = mutableListOf<User>()
        val currentUserEmail = auth.currentUser!!.email
        Log.d("firebase", "El current user es ${currentUserEmail}")
        database.child("users").get().addOnSuccessListener {
            Log.d("firebase", "Got value ${it.value}")
            val snapObject = it.children
            snapObject.forEach {
                val key = it.key.toString()
                val name = it.child("name").value.toString()
                val mail = it.child("mail").value.toString()
                val age = it.child("age").value.toString().toInt()

                val user = User(name = name, age = age, mail = mail, key = key)
                if(mail != currentUserEmail){
                    listOfUsers.add(user)
                }


                Log.d("firebase", "El user es $user")

            }

            _listOfUsers.value = listOfUsers

        }.addOnFailureListener {
            Log.d("firebase", "Error getting data", it)
        }.addOnCompleteListener {
            if (it.isComplete) {
                _tengoDatos.value = true
            }
        }


    }

    fun writeInfoToDatabase() {

        val user = User("Gonzalo Vega", 25, "gonzalo@mail.com")
        database.child("users").push().setValue(user)
    }
}

