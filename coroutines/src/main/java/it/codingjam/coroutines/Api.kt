package it.codingjam.coroutines

import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async

class Api(private val service: StackOverflowService) {
    fun login(): Deferred<Int> = async {
        service.getTopUsers().await()[0].id
    }

}