package com.sendinfo.wuzhizhou.base

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.base.library.base.BActivity
import com.base.library.mvp.BPresenter
import com.sendinfo.wuzhizhou.R
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_take_order.*

open class BaseActivity<T : BPresenter> : BActivity<T>() {

    // 没有数据
    val notDataView: View by lazy { layoutInflater.inflate(R.layout.empty_view, rv.parent as ViewGroup, false) }

    override fun initArgs(intent: Intent?) {
    }

    override fun initView() {
    }

    override fun initData() {
    }

    override fun initContentView(layoutResID: Int) {
        setContentView(R.layout.activity_base)
        val contentView = LayoutInflater.from(this).inflate(layoutResID, fl, false)
        fl.addView(contentView)
    }

}