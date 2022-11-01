package com.jiangkedev

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.ext.web.Router

/**
 * @author 姜科 <bazzjiang@hotmail.com>
 * @date 2022-08-30
 * @description
 */
@VerticleClass class MainVerticle : AbstractVerticle() {

  private val dao = IslandsDao()

  private val router = Router.router(vertx).apply {
    /**
     * Welcome handler.
     */
    get("/").handler { ctx ->
      ctx.response.end("Welcome!")
    }

    /**
     * Lists all islands.
     */
    get("/islands").handler { ctx ->
      val islands = dao.fetchIslands()
      ctx.response.endWithJson(islands)
    }

    /**
     * Lists all countries.
     */
    get("/countries").handler { ctx ->
      val countries = dao.fetchCountries()
      ctx.response.endWithJson(countries)
    }

    /**
     * Returns specific country.
     */
    get("/countries/:code").handler { ctx ->
      val code = ctx.request.getParam("code")
      val countries = dao.fetchCountries(code)

      if (countries.isEmpty()) {
        ctx.fail(404)
      } else {
        ctx.response.endWithJson(countries.first())
      }
    }
  }

  override fun start(startPromise: Promise<Void>) {
    vertx
      .createHttpServer()
      .requestHandler(router)
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}

fun main() {
  MainVerticle().start()
}
