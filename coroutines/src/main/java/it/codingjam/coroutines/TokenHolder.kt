package it.codingjam.coroutines

import android.content.SharedPreferences
import com.nalulabs.prefs.string
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.withContext

class TokenHolder(prefs: SharedPreferences) {

    private var token by prefs.string()

    suspend fun loadToken(): String {
        return withContext(CommonPool) {
            log("loadToken")
            token
        }
    }

    suspend fun saveToken(token: String) {
        withContext(CommonPool) {
            log("saveToken")
            this.token = token
        }
    }
}