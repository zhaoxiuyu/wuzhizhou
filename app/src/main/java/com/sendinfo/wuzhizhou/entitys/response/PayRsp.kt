package com.sendinfo.wuzhizhou.entitys.response

import java.io.Serializable

/**
 * Created by Administrator on 2018/6/13.
 */

class PayRsp : Serializable {
    var qrCodeId: String = ""//": "31941806130130723171196573",
    var msgType: String? = null//": "bills.GetQRCode",
    var msgSrc: String? = null//": "WWW.TEST.COM",
    var errMsg: String? = null//": "查询二维码成功",
    var mid: String? = null//": "898340149000005",
    var billDate: String? = null//": "2018-06-13",
    var tid: String? = null//": "88880001",
    var instMid: String? = null//": "QRPAYDEFAULT",
    var responseTimestamp: String? = null//": "2018-06-13 17:07:23",
    var errCode: String? = null//": "SUCCESS",
    var billStatus: String? = null//
    var billNo: String? = null//": "3194201806131707227402542292",
    var billQRCode: String = ""//": "https://qr-test2.chinaums.com/bills/qrCode.do?id=31941806130130723171196573",
    var sign: String? = null//": "22C5186F3F7170B0F63BD7DADFD54411"
}
