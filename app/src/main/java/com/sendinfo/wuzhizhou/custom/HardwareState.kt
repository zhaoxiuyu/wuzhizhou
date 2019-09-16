package com.sendinfo.wuzhizhou.custom

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.LogUtils
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 硬件状态检测,打印机 和 身份证阅读器
 */
class HardwareState : BPresenter {

    lateinit var owner: LifecycleOwner

    override fun getData(http: BRequest) {
        getDyjState()
    }

    override fun onCreate(owner: LifecycleOwner) {
        LogUtils.d("onCreate")
        this.owner = owner
    }

    override fun onResume(owner: LifecycleOwner) {
        LogUtils.d("onResume")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        LogUtils.d("onDestroy")
    }

    override fun onLifecycleChanged(owner: LifecycleOwner, event: Lifecycle.Event) {
        LogUtils.d("onLifecycleChanged")
    }

    private fun getDyjState() {
        Observable.just("")
            .map {
                LogUtils.d("状态线程 获取  : " + Thread.currentThread().name)
                "状态"
//                JsonUtils.toAny(it, BaseResponse::class.java)
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .`as`(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(owner)))
            .subscribe({
                LogUtils.d("状态线程 返回 : " + Thread.currentThread().name)
            }, {

            })
    }

}