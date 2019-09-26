package com.sendinfo.wuzhizhou.module.common.ui

import android.animation.ValueAnimator
import com.base.library.entitys.BaseResponse
import com.base.library.mvp.BPresenter
import com.base.library.mvp.BaseView
import com.github.florent37.viewanimator.ViewAnimator
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.GlideImageLoader
import com.sendinfo.wuzhizhou.utils.startAct
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<BPresenter>(), BaseView {

    override fun initView() {
        super.initView()
        setContentView(R.layout.activity_home)
    }

    override fun initData() {
        super.initData()
        initBanner()
        online.setOnClickListener {
            //            val journalRecord = JournalRecord().apply {
            //                content = "内容啊"
            //                behavior = "测试"
            //            }
//            roomLifecycle.addJournal(journalRecord)
//            roomLifecycle.removeBefore()
            startAct(this, MainActivity::class.java, isFinish = false)
        }
        ViewAnimator.animate(online)
            .scaleX(1f, 0.9f)
            .duration(1000)
            .repeatMode(ViewAnimator.REVERSE)
            .repeatCount(ValueAnimator.INFINITE)
            .start()
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