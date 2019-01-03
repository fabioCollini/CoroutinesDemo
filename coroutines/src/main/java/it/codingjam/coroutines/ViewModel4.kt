package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.*

class ViewModel4(
        private val tokenHolder: TokenHolder,
        private val api: StackOverflowService
) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext = job + Dispatchers.IO

    fun load() {
        launch {
            try {
                log("start")
                var token = tokenHolder.loadToken()
                if (token.isEmpty()) {
                    withContext(Dispatchers.Main) { updateUi("Logging in") }
                    token = api.login().await().token
                    log("where am I?")
                    tokenHolder.saveToken(token)
                }

                withContext(Dispatchers.Main) { updateUi("Loading data") }

                val data = api.loadData(token).await()

                withContext(Dispatchers.Main) { updateUi(data) }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) { updateUi(e.toString()) }
            }
        }
    }

    private fun updateUi(s: Any) {
        log("updateUi")
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
    }
}