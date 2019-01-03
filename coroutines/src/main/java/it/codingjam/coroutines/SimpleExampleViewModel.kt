package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.coroutines.utils.LiveDataDelegate
import it.codingjam.coroutines.utils.log
import kotlinx.coroutines.*

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
        GlobalScope.launch(Dispatchers.Main + job) {
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
        withContext(Dispatchers.Main) {
            log("updateUi")
            state = s.toString()
        }
    }


    override fun onCleared() {
        job.cancel()
    }
}