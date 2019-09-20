package com.sendinfo.wuzhizhou.module.take.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView
import com.sendinfo.wuzhizhou.entitys.response.TakeTicketModelsVo

class TakeDetailedContract {

    interface View : BView {
        fun toPrintTemp(printTemp: MutableList<String>)
    }

    interface Presenter : BPresenter {
        fun saveOrder(takeTicketModels: MutableList<TakeTicketModelsVo>?, uuid: String)
    }

}