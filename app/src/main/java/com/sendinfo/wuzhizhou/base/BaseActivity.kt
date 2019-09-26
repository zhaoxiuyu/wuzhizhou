package com.sendinfo.wuzhizhou.base

import android.content.Intent
import android.view.LayoutInflater
import com.base.library.base.BActivity
import com.base.library.mvp.BPresenter
import com.base.library.util.SoundPoolUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.owner.PrintStateOwner
import kotlinx.android.synthetic.main.activity_base.*

/**
 * BActivity 在 library 里面，不想动
 * 用 BaseActivity 在包一层吧
 */
open class BaseActivity<T : BPresenter> : BActivity<T>() {

    val printStateOwner: PrintStateOwner by lazy { PrintStateOwner() }
    val soundPoolUtils: SoundPoolUtils by lazy { SoundPoolUtils() }

    override fun initArgs(intent: Intent?) {
    }

    override fun initView() {
        lifecycle.addObserver(printStateOwner)
        lifecycle.addObserver(soundPoolUtils)
    }

    override fun initData() {
    }

    override fun initContentView(layoutResID: Int) {
        setContentView(R.layout.activity_base)
        val contentView = LayoutInflater.from(this).inflate(layoutResID, fl, false)
        fl.addView(contentView)
    }

}