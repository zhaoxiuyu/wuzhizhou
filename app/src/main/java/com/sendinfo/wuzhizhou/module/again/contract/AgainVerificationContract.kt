package com.sendinfo.wuzhizhou.module.again.contract

import com.base.library.mvp.BPresenter
import com.base.library.mvp.BView

/**
 * 作用: 验证管理员,自己定义Contract
 */
interface AgainVerificationContract {

    interface View : BView {
        fun loginSuccess(request: String?)

        fun loginError(msg: String?)
    }

    interface Presenter : BPresenter {
        fun login(optorCode: String, optorPwd: String)
    }

}