package com.sendinfo.wuzhizhou.module.purchase.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.entitys.response.GetTicketGroupVo

interface PurchaseMainContract {

    interface View : BView {
        fun getTicketSuccess(tickets: MutableList<GetTicketVo>)
        fun getTicketGroupSuccess(ticketGroup: MutableList<GetTicketGroupVo>)
    }

    interface Presenter : BPresenter {
        fun getTicket()
        fun getTicketGroup()
    }

}