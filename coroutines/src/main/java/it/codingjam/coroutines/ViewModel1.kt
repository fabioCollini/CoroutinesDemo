package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

class ViewModel1(
        private val tokenHolder: TokenHolder,
        private val api: StackOverflowService
) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        launch(UI + job) {
            log("start")
            try {
                var token = async { tokenHolder.loadToken() }.await()
                if (token.isEmpty()) {
                    updateUi("Logging in")
                    token = api.login().await().token
                    log("where am I?")
                    async { tokenHolder.saveToken(token) }.await()
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