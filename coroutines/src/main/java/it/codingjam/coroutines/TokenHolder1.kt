package it.codingjam.coroutines

import android.content.SharedPreferences
import com.nalulabs.prefs.string
import it.codingjam.coroutines.utils.log

class TokenHolder1(prefs: SharedPreferences) {

    private var token by prefs.string()

    fun loadToken(): String {
        log("loadToken")
        return token
    }

    fun saveToken(token: String) {
        log("saveToken")
        this.token = token
    }
}