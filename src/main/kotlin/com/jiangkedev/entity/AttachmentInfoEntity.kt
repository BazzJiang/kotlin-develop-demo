package com.jiangkedev.entity

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

/**
 *@author 姜科 <bazzjiang@hotmail.com>
 *@date 2023-02-05
 *@description 附件表
 */
@Entity
@Table(name="attachment_info")
data class AttachmentInfoEntity(

  /**
   * id
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id:String,

  /**
   * 附件名称
   */
  var attachName:String,

  /**
   * 附件扩展名
   */
  var attachExt:String,

  /**
   * md5值
   */
  var attachMd5:String,

  /**
   * 附件大小
   */
  var attachSize:Int,

  /**
   * 附件地址
   */
  var attachUrl:String,

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
  var updateTime:LocalDateTime
)
