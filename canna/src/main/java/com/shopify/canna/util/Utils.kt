package com.shopify.canna.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Utils {
    fun showToast(context: Context, message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    fun isValidEmail(email : String?): Boolean {
        if (email.isNullOrEmpty() || !email.contains("@")){
            return false
        }

        return true
    }

    fun isValidPassword(password : String?): Boolean {
        if (password.isNullOrEmpty() || password.length < 6)
            return false

        return true
    }
}