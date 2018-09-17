package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.Main
import kotlinx.coroutines.experimental.launch

class ViewModel3(
        private val tokenHolder: TokenHolder,
        private val api: StackOverflowService
) : ViewModel(), CoroutineScope {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    override val coroutineContext = job + Dispatchers.Main

    fun load() {
        launch {
            try {
                log("start")
                var token = tokenHolder.loadToken()
                if (token.isEmpty()) {
                    updateUi("Logging in")
                    token = api.login().await().token
                    log("where am I?")
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

    private fun updateUi(s: Any) {
        log("updateUi")
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
    }
}