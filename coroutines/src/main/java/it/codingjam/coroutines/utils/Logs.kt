package it.codingjam.coroutines.utils

fun log(msg: String) = println("[${Thread.currentThread().name}] $msg")
