package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.view.View
import com.base.library.mvp.BPresenter
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.github.ihsg.patternlocker.OnPatternChangeListener
import com.github.ihsg.patternlocker.PatternLockerView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.module.again.ui.AgainIdCardParamActivity
import com.sendinfo.wuzhizhou.module.again.ui.AgainPayParamActivity
import com.sendinfo.wuzhizhou.module.again.ui.AgainSettingActivity
import com.sendinfo.wuzhizhou.utils.*
import kotlinx.android.synthetic.main.activity_gesture.*

class GestureActivity : BaseActivity<BPresenter>(), OnPatternChangeListener {

    private var plvStr: String = ""
    private var isLogin: Boolean = true // true 登录验证,false 首次创建

    private var source: String = "" // 来源,payParam参数设置,home首页

    override fun initArgs(intent: Intent?) {
        super.initArgs(intent)
        intent?.let {
            source = it.getStringExtra("source")
        }
    }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_gesture)
    }

    override fun initData() {
        super.initData()

        // 为空说明是创建密码
        plvStr = getPlv()
        if (StringUtils.isEmpty(plvStr)) {
            isLogin = false
            tvInfo.text = "请创建手势密码"
        } else {
            tvInfo.text = "请验证手势密码"
            btSubmit.visibility = View.GONE
        }

        plv.setOnPatternChangedListener(this)
        btSubmit.setOnClickListener {
            plvStr = ""
            tvInfo.text = "请创建手势密码"
        }
    }

    //开始绘制图案时（即手指按下触碰到绘画区域时）会调用该方法
    override fun onStart(view: PatternLockerView) {
    }

    //图案绘制改变时（即手指在绘画区域移动时）会调用该方法，请注意只有 @param hitList改变了才会触发此方法
    override fun onChange(view: PatternLockerView, hitIndexList: List<Int>) {
    }

    //图案绘制完成时（即手指抬起离开绘画区域时）会调用该方法
    override fun onComplete(view: PatternLockerView, hitIndexList: List<Int>) {
        if (isLogin) {
            loginPlv(hitIndexList)
        } else {
            createPlv(hitIndexList)
        }
        LogUtils.d("${CollectionUtils.toString(hitIndexList)}")
    }

    //已绘制的图案被清除时会调用该方法
    override fun onClear(view: PatternLockerView) {
    }

    private fun createPlv(hitIndexList: List<Int>) {
        if (hitIndexList.size < 4) {
            tvInfo.text = "点位不能少于4个"
            YoYo.with(Techniques.Shake).playOn(tvInfo)
            return
        }

        // 为空说明是第一次绘制
        if (StringUtils.isEmpty(plvStr)) {
            plvStr = CollectionUtils.toString(hitIndexList)
            tvInfo.text = "请进行第二次绘制"
            YoYo.with(Techniques.Shake).playOn(tvInfo)
            return
        }

        //两次绘制相同
        if (plvStr == CollectionUtils.toString(hitIndexList)) {
            tvInfo.text = "密码创建成功"
            putPlv(plvStr)
            toAct()
        } else {
            tvInfo.text = "两次密码不一致"
            YoYo.with(Techniques.Shake).playOn(tvInfo)
        }
    }

    private fun loginPlv(hitIndexList: List<Int>) {
        if (plvStr == CollectionUtils.toString(hitIndexList)) {
            tvInfo.text = "登录验证成功"
            toAct()
        } else {
            tvInfo.text = "登录验证失败"
        }
    }

    private fun toAct() {
        when (source) {
            payParam -> startAct(this, AgainPayParamActivity::class.java)
            home -> startAct(this, AgainSettingActivity::class.java)
            updateIdCard -> startAct(this, AgainIdCardParamActivity::class.java)
            else -> {
                startAct(this, AgainSettingActivity::class.java)
            }
        }
    }
}