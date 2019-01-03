package it.codingjam.coroutines

import androidx.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.*

class ViewModel5(
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
                var token = tokenHolder.loadToken()
                if (token.isEmpty()) {
                    updateUi("Logging in")
                    token = api.login().await().token
                    tokenHolder.saveToken(token)
                }

                updateUi("Loading data")

                val data = api.loadData(token).await()

                updateUi(data)
            } catch (e: Exception) {
                updateUi(e.toString())
            }
        }
    }

    private suspend fun updateUi(s: Any) {
        withContext(Dispatchers.Main) {
            log("updateUi")
            state = s.toString()
        }
    }

    override fun onCleared() {
        job.cancel()
    }
}