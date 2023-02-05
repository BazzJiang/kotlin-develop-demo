package com.jiangkedev.entity

import java.time.LocalDateTime

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-05
 *@description 基础实体
 */
open class BaseEntity (

  /**
   * 删除标志位
   */
  var deleted:String,

  /**
   * 创建人
   */
  var createBy:String,

  /**
   * 创建时间
   */
  var createTime:LocalDateTime,

  /**
   * 更新人
   */
  var updateBy:String,

  /**
   * 创建时间
   */
  var updateTime:LocalDateTime,
)
