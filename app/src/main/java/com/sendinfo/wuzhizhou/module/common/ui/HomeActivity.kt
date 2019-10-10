package com.sendinfo.wuzhizhou.module.common.ui

import android.animation.ValueAnimator
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.github.florent37.viewanimator.ViewAnimator
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.GlideImageLoader
import com.sendinfo.wuzhizhou.entitys.response.Notice
import com.sendinfo.wuzhizhou.module.common.presenter.MainPresenter
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.utils.getIp
import com.sendinfo.wuzhizhou.utils.startAct
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<MainContract.Presenter>(), MainContract.View {

    private var notice: Notice? = null

    override fun initView() {
        super.initView()
        setContentView(R.layout.activity_home)

        mPresenter = MainPresenter(this)
        initBanner()
    }

    override fun initData() {
        super.initData()
        online.setOnClickListener {
            startAct(this, Intent(this, MainActivity::class.java).putExtra("notice", notice), isFinish = false)
//            startAct(this, MainActivity::class.java, isFinish = false)
        }
        ViewAnimator.animate(online)
            .scaleX(1f, 0.9f)
            .duration(1000)
            .repeatMode(ViewAnimator.REVERSE)
            .repeatCount(ValueAnimator.INFINITE)
            .start()
    }

    override fun onResume() {
        super.onResume()
        // 如果IP端口为空就跳转设置页面
        if (StringUtils.isEmpty(getIp())) {
            startAct(this, GestureActivity::class.java, isFinish = false)
        } else {
            mPresenter?.queryNotice()
        }

        banner.start()
    }

    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR)
        banner.setImageLoader(GlideImageLoader())
        banner.setBannerAnimation(Transformer.DepthPage)
        banner.isAutoPlay(true)
        banner.setDelayTime(3000)
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        banner.start()
    }

    override fun queryNoticeSuccess(notice: Notice) {
        this.notice = notice
        val ip = getIp()
        val images = mutableListOf<String>()
        notice.SplashImages?.forEach { images.add("$ip$it") }
        banner.update(images)
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