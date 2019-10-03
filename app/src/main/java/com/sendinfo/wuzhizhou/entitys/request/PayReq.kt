package com.sendinfo.wuzhizhou.entitys.request

import java.io.Serializable

/**
 * Created by Administrator on 2018/5/22.
 * 银商支付
 */

class PayReq : Serializable {
    var qrCodeId: String? = null//
    var systemId: String? = null //
    var msgType: String? = null//消息类型  GetQRCode
    var counterNo: String? = null// 请求 预留位
    var requestTimestamp: String? = null//yyyy-MM-dd HH:mm:ss

    var billDes: String? = null//消息id，
    var msgSrc: String? = null //消息 来源
    var sign: String? = null//消息类型  GetQRCode

    var goods: YsGoods? = null
    var mid: String? = null//": "898310060514001",
    var msgId: String? = null//": "800000000010",
    var billDate: String? = null//": "2016-09-22",
    var tid: String? = null//": "0001",
    var instMid: String? = null//": "QRPAYDEFAULT",
    var totalAmount: String? = null//": "1",
    var notifyUrl: String? = null

    var billNo: String? = null

    class YsGoods : Serializable {
        var quantity: String? = null//":"1",
        var goodsId: String? = null//":"0002",   商品id
        var price: String? = null//":"100", //商品单价
        var goodsCategory: String? = null//":"Auto", 商品分类
        var body: String? = null//":"goods body", 商品说明
        var goodsName: String? = null//":"Goods Name" //商品名称
    }
}
