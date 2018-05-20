package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SimpleExampleViewModel(
        private val tokenHolder: TokenHolder,
        private val api: StackOverflowService
) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
//    launch(UI + job) {
//      try {
//        log("start")
//        val token = api.login().await().token
//        log("where am I?")
//        val data = api.loadData(token).await()
//        updateUi(data)
//      } catch (e: Exception) {
//        updateUi(e.toString())
//      }
//    }
        launch(CommonPool + job) {
            try {
                log("start")
                val token = api.login().await().token
                log("where am I?")
                val data = api.loadData(token).await()
                updateUi(data)
            } catch (e: Exception) {
                updateUi(e.toString())
            }
        }
    }

    private suspend fun updateUi(s: Any) {
        withContext(UI) {
            log("updateUi")
            state = s.toString()
        }
    }


    override fun onCleared() {
        job.cancel()
    }
}