package it.codingjam.coroutines

import android.arch.lifecycle.ViewModel
import it.codingjam.common.arch.LiveDataDelegate
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class ViewModel1(private val service: StackOverflowService) : ViewModel() {

    val liveDataDelegate = LiveDataDelegate("")

    var state by liveDataDelegate

    private val job = Job()

    fun load() {
        async(UI + job) {
            try {
                val users = service.getTopUsers().await()
                val firstUser = users.first()
                val badges = service.getBadges(firstUser.id).await()
                updateUi(badges)
            } catch (e: Exception) {
                updateUi(e)
            }
        }
    }

    private fun updateUi(s: Any) {
        state = s.toString()
    }


    override fun onCleared() {
        job.cancel()
    }
}