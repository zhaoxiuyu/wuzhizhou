package com.sendinfo.wuzhizhou.module.again.presenter

import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenterImpl
import com.sendinfo.wuzhizhou.module.again.contract.AgainRecordContract

class AgainRecordPresenter(view: AgainRecordContract.View) :
    BPresenterImpl<AgainRecordContract.View>(view), AgainRecordContract.Presenter {

    override fun check(idCard: String) {
    }

    override fun requestSuccess(baseResponse: BaseResponse, baseHttpDto: BRequest) {
        super.requestSuccess(baseResponse, baseHttpDto)

    }

}