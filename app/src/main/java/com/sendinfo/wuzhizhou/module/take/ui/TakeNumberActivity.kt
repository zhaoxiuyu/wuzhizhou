package com.sendinfo.wuzhizhou.module.take.ui

import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity

/**
 * 辅助吗
 */
class TakeNumberActivity : BaseActivity<BPresenter>(), BaseView {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_take_number)
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

}
