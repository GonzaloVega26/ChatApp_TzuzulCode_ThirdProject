package com.gonzalo.chatapp_tzuzulcode_thirdproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gonzalo.chatapp_tzuzulcode_thirdproject.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AuthViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private var auth = Firebase.auth
    private val TAG = "AUTH"


    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    private val _isRegistered = MutableLiveData(false)
    val isRegistered: LiveData<Boolean>
        get() = _isRegistered


    private val _errorMessage = MutableLiveData("")
    val errorMessage: LiveData<String>
        get() = _errorMessage


    init {
        if (auth.currentUser != null) {
            _isLoggedIn.value = true
        }
    }

    fun navigateToRegister() {
        _isRegistered.value = true
    }

    fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")


                    _isLoggedIn.value = true

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _errorMessage.value = "The Mail or Password is incorrect"

                }
            }
    }

    fun createUser(email:String, age: Int, name: String){
        val user = User(name = name, age = age, mail = email)

        database.child("users").push().setValue(user)
    }


    fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    _isLoggedIn.value = true
                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _errorMessage.value = "The Mail or Password is incorrect"
                    //updateUI(null)
                }
            }
    }


}