package com.jiangkedev

import io.vertx.core.Vertx
import io.vertx.ext.web.Router

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2022-11-03
 *@description
 */

public fun hello(vertx: Vertx):Router{
  return Router.router(vertx).apply {
    get("/hello").handler { ctx ->
      ctx.response.end("hello!")
    }
  }
}
