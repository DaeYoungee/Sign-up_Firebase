package com.example.signup.ui

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

suspend fun printRyan(): String {
    delay(1000)
    return "Ryan!"
}

suspend fun printRrodo(): String {
    delay(3000)
    return "Frodo"
}

fun printFriends() {
    val ryan = GlobalScope.async { printRyan() }
    val frodo = GlobalScope.async { printRrodo() }
    GlobalScope.launch {
        val time = measureTimeMillis {
            println("Hello, 이건 출력이 먼저 되나?")
            println("Hello, ${ryan.await()}")
            println("Hello, ${frodo.await()}")


        }
        println("ryan 스레드시간  $time")
    }

}

fun main() {
    printFriends()
    println("메인 스레드 작동중")

    readLine()
}



