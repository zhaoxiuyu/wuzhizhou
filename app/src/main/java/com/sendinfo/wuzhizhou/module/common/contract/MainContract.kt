package com.sendinfo.wuzhizhou.module.purchase.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.response.GetTicketVo
import com.sendinfo.wuzhizhou.entitys.response.GetTicketGroupVo
import com.sendinfo.wuzhizhou.entitys.response.Notice

interface MainContract {

    interface View : BView {
        fun queryNoticeSuccess(notice: Notice)
    }

    interface Presenter : BPresenter {
        fun queryNotice()
    }

}