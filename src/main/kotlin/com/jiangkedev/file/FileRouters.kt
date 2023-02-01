package com.jiangkedev.file

import com.jiangkedev.endWithJson
import com.jiangkedev.file.dto.AttachmentInfoDto
import com.jiangkedev.request
import com.jiangkedev.response
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.mysqlclient.MySQLPool
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.SqlConnection

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-01
 *@description
 */
fun fileRouters(vertx: Vertx,pool : MySQLPool?):Router{
  return Router.router(vertx).apply {
    /**
     * 根据id查询附件信息
     */
    get("/file/getInfoById/:id").handler { ctx ->
      val id = ctx.request.getParam("id")
      pool?.connection?.compose { conn: SqlConnection ->
        conn
          .query("SELECT * FROM common_attachment where id = ${id}")
          .execute()
          .onComplete { ar: AsyncResult<RowSet<Row>> ->
            if (ar.succeeded()) {
              //打印查询结果
              ar.result().forEach { row -> run { println(row.getString("attach_name")) } }
              val result : MutableList<JsonObject> = ArrayList();
              ar.result().forEach { row -> run { result.add(row.toJson()) } }
              ctx.response.endWithJson(result)
            } else {
              println("Something went wrong " + ar.cause().message)
            }
          }
      }
    }
  }
}
