package com.sendinfo.wuzhizhou.module.common.ui

import android.os.Bundle
import com.base.library.base.BFragment
import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BasePresenter
import com.base.library.mvp.BaseView
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.R
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class MainFragment : BFragment<BPresenter>(), BaseView {

    override fun initArgs(bundle: Bundle?) {
    }

    override fun initView(bundle: Bundle?) {
        setContentView(R.layout.activity_main)
        mPresenter = BasePresenter(this)
    }

    override fun initData() {
        Observable.interval(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe {
                LogUtils.d("$it")
            }
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }
}