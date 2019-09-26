package com.sendinfo.wuzhizhou.module.again.ui

import com.base.library.mvp.BPresenter
import com.sendinfo.honeywellprintlib.print.PrintReturnCallback
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.utils.HardwareExample
import com.sendinfo.wuzhizhou.utils.defaultTemplate
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_again_print.*

/**
 * 测试打印机
 */
class AgainPrintActivity : BaseActivity<BPresenter>() {

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_again_print)
    }

    override fun initData() {
        super.initData()
        print()
    }

    private fun print() {
        Observable.just(defaultTemplate()).map {
            val printReturnCallback = PrintReturnCallback {}
            HardwareExample.getWhPrint().print("", printReturnCallback, 15000)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this)))
            .subscribe({
                tvInfo.text = it.toString()
            }, {
                it.printStackTrace()
            })
    }

    override fun onStop() {
        super.onStop()
        lav.cancelAnimation()
    }

}
