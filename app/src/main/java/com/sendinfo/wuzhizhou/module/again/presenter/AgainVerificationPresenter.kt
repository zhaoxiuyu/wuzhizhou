package com.sendinfo.wuzhizhou.module.again.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.blankj.utilcode.util.EncryptUtils
import com.sendinfo.wuzhizhou.module.again.contract.AgainVerificationContract
import com.sendinfo.wuzhizhou.utils.Login

class AgainVerificationPresenter(view: AgainVerificationContract.View) :
    BPresenterImpl<AgainVerificationContract.View>(view), AgainVerificationContract.Presenter {

    override fun login(optorCode: String, optorPwd: String) {
        val map = mapOf("optorCode" to optorCode, "optorPwd" to EncryptUtils.encryptMD5ToString(optorPwd).toUpperCase())
        val bRequest = BRequest(Login).apply {
            params = map
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, bHttpDto: BRequest) {
        super.requestSuccess(baseResponse, bHttpDto)
        if (baseResponse.success) {
            mView?.loginSuccess(baseResponse.message)
        } else {
            mView?.loginError(baseResponse.message)
        }
    }

}