package com.sendinfo.wuzhizhou.module.again.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.blankj.utilcode.util.StringUtils
import com.sendinfo.wuzhizhou.module.again.contract.AgainVerificationContract

class AgainVerificationPresenter(view: AgainVerificationContract.View) :
    BPresenterImpl<AgainVerificationContract.View>(view), AgainVerificationContract.Presenter {

    override fun check(idCard: String) {
        if (StringUtils.isEmpty(idCard)) {
            mView?.checkError("请刷身份证")
        } else {
            val map = mapOf("idCard" to idCard)
            val bRequest = BRequest("url").apply {
                params = map
            }
            getData(bRequest)
        }
    }

    override fun requestSuccess(baseResponse: BaseResponse, baseHttpDto: BRequest) {
        super.requestSuccess(baseResponse, baseHttpDto)

    }

}