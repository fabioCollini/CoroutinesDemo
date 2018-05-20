package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class ViewModel4(
        private val tokenHolder: TokenHolder,
        private val api: StackOverflowService
) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        launch(CommonPool + job) {
            try {
                log("start")
                var token = tokenHolder.loadToken()
                if (token.isEmpty()) {
                    withContext(UI) { updateUi("Logging in") }
                    token = api.login().await().token
                    log("where am I?")
                    tokenHolder.saveToken(token)
                }

                withContext(UI) { updateUi("Loading data") }

                val data = api.loadData(token).await()

                withContext(UI) { updateUi(data) }
            } catch (e: Exception) {
                withContext(UI) { updateUi(e.toString()) }
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