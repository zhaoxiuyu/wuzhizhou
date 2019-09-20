package com.sendinfo.wuzhizhou.module.again.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView

/**
 * 作用: 验证管理员,自己定义Contract
 */
interface AgainVerificationContract {

    interface View : BView {
        fun checkSuccess(request: String?)

        fun checkError(msg: String?)
    }

    interface Presenter : BPresenter {
        fun check(idCard: String)
    }

}