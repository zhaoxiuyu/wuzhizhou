package com.sendinfo.wuzhizhou.module.purchase.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.sendinfo.wuzhizhou.module.purchase.contract.PurchaseSureContract
import com.sendinfo.wuzhizhou.utils.ValidateBacklist

class PurchaseSurePresenter(view: PurchaseSureContract.View) :
    BPresenterImpl<PurchaseSureContract.View>(view), PurchaseSureContract.Presenter {

    override fun ValidateBacklist(certno: String) {
        val map = mapOf("certno" to certno)
        val bRequest = BRequest(ValidateBacklist).apply {
            httpType = BRequest.GET
            params = map
            silence = true
        }
        getData(bRequest)
    }

    override fun requestSuccess(baseResponse: BaseResponse, baseHttpDto: BRequest) {
        super.requestSuccess(baseResponse, baseHttpDto)
        if (baseResponse.success) {
            when (baseHttpDto.method) {
                ValidateBacklist -> {
                    if ("0" == "${baseResponse.data}") {
                        mView?.ValidateBacklistSuccess()
                    } else {
                        mView?.ValidateBacklistError(baseResponse.message)
                    }
                }
                else -> {
                }
            }
        } else {
            mView?.ValidateBacklistError(baseResponse.message)
        }
    }

    override fun requestError(throwable: Throwable?, baseHttpDto: BRequest) {
        super.requestError(throwable, baseHttpDto)
        when (baseHttpDto.url) {
            ValidateBacklist -> {
                mView?.ValidateBacklistError("网络错误")
            }
        }
    }

}