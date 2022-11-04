package com.jiangkedev

import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.mysqlclient.MySQLConnectOptions
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.PoolOptions
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlConnection


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

    /**
     * 基本查询
     */
    get("/mysql/query").handler{ctx->
      pool?.connection?.compose { conn: SqlConnection ->
        println("Got a connection from the pool")
        conn
          .query("SELECT * FROM fhrs_dict_sex")
          .execute()
          .onComplete { ar: AsyncResult<RowSet<Row>>? ->
            // Release the connection to the pool
            conn.close()
          }
      }?.onComplete { ar: AsyncResult<RowSet<Row>> ->
        if (ar.succeeded()) {
          println("Done")
        } else {
          println("Something went wrong " + ar.cause().message)
        }
      }
    }
  }

  //连接池配置
  public val poolOptions = PoolOptions().setMaxSize(5)

  //mysql连接配置
  private val connectOptions = MySQLConnectOptions()
    .setPort(4308)
    .setHost("100.69.13.43")
    .setDatabase("fhms")
    .setUser("deployop")
    .setPassword("Cjcs1hp!");

  private var pool: MySQLPool? = null


  override fun start(startPromise: Promise<Void>) {
    this.pool = MySQLPool.pool(vertx,connectOptions,poolOptions)
    vertx
      .createHttpServer()
      .requestHandler(router)
      .requestHandler(hello(vertx))
      .listen(18889) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}
