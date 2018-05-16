package post

import kotlinx.coroutines.experimental.*
import org.junit.Test
import java.util.concurrent.Executors

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")

class MyPrefs {
  fun token(): String? {
//    log("Loading token")
    return null
  }

  fun saveToken(token: String) {
//    log("Saving token")
  }

  suspend fun token2(): String? = withContext(CommonPool) {
//    log("Loading token")
    null
  }

  suspend fun saveToken2(token: String) = withContext(CommonPool) {
//    log("Saving token")
  }
}

class MyData {
  override fun toString() = "MyData"
}

class OtherData {
  override fun toString() = "OtherData"
}

class MyApi {
  fun login(): Deferred<String> = async {
    log("logging in")
    "token"
  }

  fun loadData(token: String): Deferred<MyData> = async {
    log("loading data")
    MyData()
  }

  fun loadOtherData(token: String): Deferred<OtherData> = async {
    log("loading other data")
    OtherData()
  }
}

val UI = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

class MyInteractor(
    private val prefs: MyPrefs,
    private val api: MyApi
) {
  suspend fun loadWithePrefsInUI() {
    launch(UI + CoroutineName("UI")) {
      var token = prefs.token()
      if (token == null) {
        updateUi("Logging in")
        token = api.login().await()
        prefs.saveToken(token)
      }

      updateUi("Loading data")

      val data = api.loadData(token).await()
      val otherData = api.loadOtherData(token).await()

      updateUi("$data $otherData")
    }.join()
  }

  suspend fun loadWithePrefsInCommonPool() {
    launch(UI + CoroutineName("UI")) {
      log("start")
      var token = async { prefs.token() }.await()
      if (token == null) {
        updateUi("Logging in")
        token = api.login().await()
        log("where am I?")
        async { prefs.saveToken(token) }.await()
      }

      updateUi("Loading data")

      val data = api.loadData(token).await()
      log("where am I?")
      val otherData = api.loadOtherData(token).await()

      updateUi("$data $otherData")
    }.join()
  }

  suspend fun loadWithePrefsInCommonPoolWithContext() {
    launch(UI + CoroutineName("UI")) {
      log("start")
      var token = withContext(CommonPool) { prefs.token() }
      if (token == null) {
        updateUi("Logging in")
        token = api.login().await()
        log("where am I?")
        withContext(CommonPool) { prefs.saveToken(token) }
      }

      updateUi("Loading data")

      val data = api.loadData(token).await()
      log("where am I?")
      val otherData = api.loadOtherData(token).await()

      updateUi("$data $otherData")
    }.join()
  }

  suspend fun loadWithePrefsInCommonPoolWithContextInMethod() {
    launch(UI + CoroutineName("UI")) {
      log("start")
      var token = prefs.token2()
      if (token == null) {
        updateUi("Logging in")
        token = api.login().await()
        log("where am I?")
        prefs.saveToken2(token)
      }

      updateUi("Loading data")

      val data = api.loadData(token).await()
      log("where am I?")
      val otherData = api.loadOtherData(token).await()

      updateUi("$data $otherData")
    }.join()
  }

  suspend fun loadInCommonPool() {
    launch(CommonPool) {
      log("start")
      var token = prefs.token2()
      if (token == null) {
        withContext(UI + CoroutineName("UI")) {
          updateUi("Logging in")
        }
        token = api.login().await()
        log("where am I?")
        prefs.saveToken2(token)
      }

      withContext(UI + CoroutineName("UI")) {
        updateUi("Loading data")
      }

      val data = api.loadData(token).await()
      log("where am I?")
      val otherData = api.loadOtherData(token).await()

      withContext(UI + CoroutineName("UI")) {
        updateUi("$data $otherData")
      }
    }.join()
  }

  suspend fun loadInCommonPool2() {
    launch(CommonPool) {
      log("start")
      var token = prefs.token2()
      if (token == null) {
        updateUi2("Logging in")
        token = api.login().await()
        log("where am I?")
        prefs.saveToken2(token)
      }

      updateUi2("Loading data")

      val data = api.loadData(token).await()
      log("where am I?")
      val otherData = api.loadOtherData(token).await()

      updateUi2("$data $otherData")
    }.join()
  }

  fun updateUi(message: String) {
//    log(message)
  }

  suspend fun updateUi2(message: String) = withContext(UI + CoroutineName("UI")) {
    //    log(message)
  }
}

class Example {
  @Test fun name() = runBlocking {
    val interactor = MyInteractor(MyPrefs(), MyApi())

//    interactor.loadWithePrefsInUI()
    interactor.loadWithePrefsInCommonPool()
    println("--------------------------------------------")
    println("--------------------------------------------")
    interactor.loadWithePrefsInCommonPoolWithContext()
    println("--------------------------------------------")
    interactor.loadInCommonPool()
  }
}