package com.jiangkedev.file

import com.jiangkedev.ServiceEndpoint
import com.jiangkedev.entity.AttachmentInfoEntity
import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.ext.web.Router
import org.hibernate.reactive.mutiny.Mutiny

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-09
 *@description 提供文件上传服务
 */
class FileService: ServiceEndpoint {


  override fun mountPoint(): String {
    return "/file"
  }

  override fun router(vertx: Vertx,emf: Mutiny.SessionFactory?): Router {
    return Router.router(vertx).apply {
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
}
