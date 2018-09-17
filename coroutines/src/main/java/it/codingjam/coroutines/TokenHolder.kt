package it.codingjam.coroutines

import android.content.SharedPreferences
import com.nalulabs.prefs.string
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.IO
import kotlinx.coroutines.experimental.withContext

class TokenHolder(prefs: SharedPreferences) {

    private var token by prefs.string()

    suspend fun loadToken(): String {
        return withContext(Dispatchers.IO) {
            log("loadToken")
            token
        }
    }

    suspend fun saveToken(newToken: String) {
        withContext(Dispatchers.IO) {
            log("saveToken")
            token = newToken
        }
    }
}