package com.sendinfo.wuzhizhou.module.purchase.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView

class PurchaseSureContract {

    interface View : BView {
        fun ValidateBacklistSuccess()
        fun ValidateBacklistError(msg: String)
    }

    interface Presenter : BPresenter {
        fun ValidateBacklist(certno: String)
    }

}