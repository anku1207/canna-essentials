package com.shopify.canna.util

import com.google.gson.Gson
import com.shopify.buy3.Storefront
import com.shopify.canna.BaseApplication
import com.shopify.canna.SampleApplication

object Prefs {
    private const val PREFS_FILENAME = "com.shopify.canna"
    private const val ACCESS_TOKEN = "access_token"
    private const val TOKEN_EXPIRY = "token_expiry"
    private const val CUSTOMER_DETAILS = "customer_details"

    private val prefs = BaseApplication.instance().getSharedPreferences(PREFS_FILENAME, 0)

    var accessToken : String?
    get() = prefs.getString(ACCESS_TOKEN, "")
    set(value) = prefs.edit().putString(ACCESS_TOKEN, value).apply()

    var tokenExpiryTimestamp : Long
    get() = prefs.getLong(TOKEN_EXPIRY, 0L)
    set(value) = prefs.edit().putLong(TOKEN_EXPIRY, value).apply()

    fun fetchCustomerDetails() : Storefront.Customer?{
        val customer = prefs.getString(CUSTOMER_DETAILS, "")
        return if (customer != null && customer.length >1){
            Gson().fromJson(customer, Storefront.Customer::class.java)
        }else{
            null
        }
    }

    fun storeCustomerDetails(customer : Storefront.Customer){
        prefs.edit().putString(CUSTOMER_DETAILS, Gson().toJson(customer)).apply()
    }

    fun clear(){
        prefs.edit().clear().apply()
    }
}