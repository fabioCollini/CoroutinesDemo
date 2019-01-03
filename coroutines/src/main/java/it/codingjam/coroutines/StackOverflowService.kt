package it.codingjam.coroutines

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

data class LoginResponse(var token: String)

data class MyData(var data: String)

interface StackOverflowService {

    @GET("/fabioCollini/CoroutinesDemo/master/data/login.json")
    fun login(): Deferred<LoginResponse>

    @GET("/fabioCollini/CoroutinesDemo/master/data/data.json")
    fun loadData(@Query("token") token: String): Deferred<MyData>
}
