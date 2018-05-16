esempio con launch(UI), prefs async
se cancellato async è una nuova coroutine (no problem with retrofit)
per fixare si può passare il parent

--> withContext per prefs
    esegue la stessa coroutine su un nuovo thread
    è la stessa coroutine, la cancellazione funziona

withContext può essere spostato nel metodo per essere sicuri che non venga mai eseguito nel main thread

quando servono nested async/launch? Raramente!

aggiungiamo where am I? back and forth in threads

launch(CommonPool) come root con withContext(UI) (che può essere spostato nei metodi 
per essere sicuri che verrà sempre eseguito sulla ui)


