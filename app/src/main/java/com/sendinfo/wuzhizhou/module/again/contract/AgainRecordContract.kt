package com.sendinfo.wuzhizhou.module.again.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView

interface AgainRecordContract {

    interface View : BView {
        fun checkSuccess(request: String?)

        fun checkError(msg: String?)
    }

    interface Presenter : BPresenter {
        fun check(idCard: String)
    }

}