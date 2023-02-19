package com.jiangkedev.learn

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-12
 *@description
 */
fun main() =  runBlocking<Unit>{
  val job = GlobalScope.launch {
    delay(1000)
    println("World!")
  }
  println("Hello!")
  job.join()
}
