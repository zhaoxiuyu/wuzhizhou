package com.sendinfo.wuzhizhou.module.common.ui

import android.content.Intent
import android.view.View
import com.base.library.entitys.BaseResponse
import com.base.library.http.BRequest
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.blankj.utilcode.util.LogUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.GlideImageLoader
import com.sendinfo.wuzhizhou.custom.HardwareState
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<BPresenter>(), BaseView {

    private val hardwareState: HardwareState by lazy { HardwareState() }

    override fun initView() {
        super.initView()
        initContentView(R.layout.activity_home)
        lifecycle.addObserver(hardwareState)
    }

    override fun initData() {
        super.initData()
        initBanner()
        online.setOnClickListener {
            //            startActivity(Intent(this, MainActivity::class.java))
            hardwareState.getData(BRequest(""))
        }
        fv.setDouble(View.OnClickListener {
            LogUtils.d("三击事件")
        })
    }

    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR)
        banner.setImageLoader(GlideImageLoader())
        banner.setImages(listOf(R.mipmap.banner11, R.mipmap.banner12))
        banner.setBannerAnimation(Transformer.DepthPage)
        banner.isAutoPlay(true)
        banner.setDelayTime(3000)
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        banner.start()
    }

    override fun bindData(baseResponse: BaseResponse) {
    }

    override fun bindError(string: String) {
    }

    override fun onResume() {
        super.onResume()
        banner.start()
    }

    override fun onPause() {
        super.onPause()
        banner.stopAutoPlay()
    }

    override fun onDestroy() {
        super.onDestroy()
        banner.releaseBanner()
    }

}