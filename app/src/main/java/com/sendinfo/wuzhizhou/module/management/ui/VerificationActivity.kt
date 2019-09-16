package com.sendinfo.wuzhizhou.module.management.ui

import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity

/**
 * 验证管理员
 */
class VerificationActivity : BaseActivity<BPresenter>(), BaseView {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_verification)
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
