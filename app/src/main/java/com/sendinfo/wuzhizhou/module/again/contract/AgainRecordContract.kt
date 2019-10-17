package com.sendinfo.wuzhizhou.module.again.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.response.PrintTempVo
import com.sendinfo.wuzhizhou.entitys.response.QueryTradeVo

interface AgainRecordContract {

    interface View : BView {

        fun queryTradeSuccess(queryTradeVo: MutableList<QueryTradeVo>)

        fun reprintTicketSuccess(printTemp: MutableList<PrintTempVo>)

    }

    interface Presenter : BPresenter {

        fun queryTrade(tradeId: String, certNo: String, assistCheckNo: String)

        fun reprintTicket(oldBarcode: String)

    }

}