package com.jiangkedev

import io.vertx.mutiny.core.Vertx
import io.vertx.mutiny.ext.web.Router
import org.hibernate.reactive.mutiny.Mutiny

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-09
 *@description 解决方案参考：https://www.devcon5.ch/en/blog/2017/09/15/vertx-modular-router-design/
 */
interface ServiceEndpoint{
  /**
   * 挂载点
   */
  fun mountPoint(): String

  /**
   * 路由
   */
  fun router(vertx: Vertx,emf: Mutiny.SessionFactory?): Router
}
