package com.sendinfo.wuzhizhou.module

import com.blankj.utilcode.util.TimeUtils
import java.util.*

/**
 * 语音文件说明
 *      (fuzhuma = 请输入辅助码)
 *      (weixinpay = 请打开微信进行扫码支付)
 *      (zhifubaopay = 请打开支付宝进行扫码支付)
 *      (wxorzybsaomazhifu = 打开微信或支付宝进行扫码支付)
 *      (qrcode = 请将二维码放置于扫描区域)
 *      (idcard = 请将身份证放置于感应区)
 *      (order = 请选择订单)
 *      (take = 请选择你要取的票型)
 *      (purchase = 请选择要购买的票型)
 *      (idcarname = 请进行实名认证)
 *      (paytype = 请选择支付方式)
 *      (chupiao = 正在出票，请留意自助机下方出票口，下方出票口取票)
 *      (taketype = 请选择取票方式)
 *      (qpslcgxz = 您的取票数量超过限制数量，请到柜台取票)
 *      (yueduxuzhi = 请仔细阅读须知)
 *      (piaozhibuzu = 可打印票纸数量不足，请联系景区管理人员)
 */
fun main(args: Array<String>) {
    System.out.println(UUID.randomUUID().toString().replace("-", ""))
    System.out.println(TimeUtils.date2String(Date(), "yyyyMMddmmHHssSSS") + ((Math.random() * 9 + 1) * 1000000).toInt())

}
