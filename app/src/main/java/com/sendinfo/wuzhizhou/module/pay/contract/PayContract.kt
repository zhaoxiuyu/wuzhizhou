package com.sendinfo.wuzhizhou.module.pay.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.request.SaveOrderReq
import com.sendinfo.wuzhizhou.entitys.response.PayRsp
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo

interface PayContract {

    interface View : BView {
        fun onGetQRCode(payRsp: PayRsp)
        fun onQuery(payRsp: PayRsp)
        fun onCloseQRCode()
        fun trainQueryOrder()
        fun toPrintTemp(printTemp: MutableList<PrintTempVo>)
    }

    interface Presenter : BPresenter {
        /**
         * 发起网络请求 flag:1获取支付二维码，2查询支付结果，3关闭订单
         */
        fun httpData(
            billno: String,
            mid: String,
            instMid: String,
            msgSrc: String,
            tid: String,
            paySum: Double = 0.0,
            qrcodeID: String = "",
            secretKey: String,
            flag: Int
        )

        fun saveOrder(saveOrderVo: SaveOrderReq)

    }
}