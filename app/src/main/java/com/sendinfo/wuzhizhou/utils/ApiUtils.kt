package com.sendinfo.wuzhizhou.utils

// 获取可售票型接口
const val GetTicket = "api/v1/TicketTrade/GetTicket"

// 获取票型分组
const val GetTicketGroup = "api/v1/TicketTrade/GetTicketGroup"

/**
 * 取票的查询接口
 * takeFlag 1:身份证取票，2：二维码取票，3：辅助码取票，4：订单号取票
 * keyword 对应的值 身份证号 二维码 辅助码 订单号取票
 * lockGuid 客户端生成一个唯一的guid,这个作为锁单的值,提交订单要用到,防止并发取票用
 * --测试订单
 * --身份证号 330326199304202419（有两单） 330106201805296226（只有一单）
 * --二维码 WT120181031201566171 WT120181031200383319 WT120181127200810378
 * --辅助码 200383319 201566171 200810378
 */
const val GetTakeOrderInfo = "api/v1/TicketTrade/GetTakeOrderInfo"
const val TakeOrderInfo1 = "1"
const val TakeOrderInfo2 = "2"
const val TakeOrderInfo3 = "3"
const val TakeOrderInfo4 = "4"

/**
 * 保存订单,取票 和购票支付完成之后 都调用这个接口
 */
const val SaveOrder = "/api/v1/TicketTrade/SaveOrder"



