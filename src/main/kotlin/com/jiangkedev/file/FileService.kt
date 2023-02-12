package com.jiangkedev.file

import com.google.common.hash.HashCode
import com.google.common.hash.Hashing
import com.google.common.io.ByteSource
import com.google.common.io.Files
import com.jiangkedev.ServiceEndpoint
import com.jiangkedev.entity.AttachmentInfoEntity
import io.vertx.core.json.JsonObject
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.core.buffer.Buffer
import io.vertx.mutiny.core.file.AsyncFile
import io.vertx.mutiny.ext.web.Router
import org.hibernate.reactive.mutiny.Mutiny
import java.util.*


/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-09
 *@description 提供文件上传服务
 */
class FileService: ServiceEndpoint {

  val uploadPath = "/app/file"

  override fun mountPoint(): String {
    return "/file"
  }

  override fun router(vertx: Vertx,emf: Mutiny.SessionFactory?): Router {
    return Router.router(vertx).apply {

      /**
       * 附件上传接口
       */
      post("/upload")
        .handler{ ctx->
          //处理文件上传
          val request = ctx.request()
          request.setExpectMultipart(true)
          request.uploadHandler{upload ->
            val attachName = upload.filename()
            val attachSize = upload.size()
            val file: AsyncFile = upload.file()
            val buffer: Buffer = Buffer.buffer()
            file.toBlockingStream().forEach { b->buffer.appendBuffer(b) }
            val byteSource: ByteSource = ByteSource.wrap(buffer.bytes)
            val ext = Files.getFileExtension(attachName)
            val hc: HashCode = byteSource.hash(Hashing.md5())
            val checksum: String = hc.toString()
            file.toBlockingStream()
            println("上传文件名称:$attachName")
          }
          ctx.json(JsonObject().put("status", 200).put("msg", "upload successed!"))
        }.failureHandler { _->
          println("upload failed!")
        }

      /**
       * 文件下载
       */
      get("/download").handler{ctx->
        val request = ctx.request();
        request.response()
        .putHeader("content-type", "application/octet-stream")
        .sendFile("C:\\Users\\admin\\Desktop\\nimbus.log");
    };


      /**
       * 根据id查询附件信息
       */
      get("/getAttachmentInfoById/:id").respond { ctx ->
        val id = ctx.request().getParam("id")
        emf!!.withSession { session: Mutiny.Session ->
          session.find(AttachmentInfoEntity::class.java,id)
        }
      }
    }
  }

  /**
   * 生成存储文件名
   * 存储文件名称=文件名+md5值+时间戳+随机数+扩展名
   */
  private fun generateStoreFilename(simpleFilename: String, md5Str: String, ext: String): String {
    return "${simpleFilename}_${md5Str}_${System.currentTimeMillis()}_${Random().nextInt(1000)}_.$ext"
  }
}
