package com.sendinfo.wuzhizhou.module.common.ui

import android.animation.ValueAnimator
import android.content.Intent
import com.base.library.util.JsonUtils
import com.blankj.utilcode.util.BusUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.StringUtils
import com.github.florent37.viewanimator.ViewAnimator
import com.google.gson.reflect.TypeToken
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.impl.ConfirmPopupView
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.base.BaseActivity
import com.sendinfo.wuzhizhou.custom.GlideImageLoader
import com.sendinfo.wuzhizhou.entitys.hardware.CardInfo
import com.sendinfo.wuzhizhou.entitys.response.Notice
import com.sendinfo.wuzhizhou.interfaces.IdCardListener
import com.sendinfo.wuzhizhou.module.common.presenter.MainPresenter
import com.sendinfo.wuzhizhou.module.purchase.contract.MainContract
import com.sendinfo.wuzhizhou.owner.IdCardOwner
import com.sendinfo.wuzhizhou.service.BeatService
import com.sendinfo.wuzhizhou.utils.*
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity<MainContract.Presenter>(), MainContract.View, IdCardListener {

    private var notice: Notice? = null
    private val idCardOwner: IdCardOwner by lazy { IdCardOwner(this) }

    override fun initView() {
        super.initView()
        setContentView(R.layout.activity_home)
        initBanner()

        mPresenter = MainPresenter(this)
        lifecycle.addObserver(idCardOwner)

        // 进入
        online.setOnClickListener {
            startAct(
                this,
                Intent(this, MainActivity::class.java).putExtra("notice", notice),
                isFinish = false
            )
        }

        // 进入 按钮动画
        ViewAnimator.animate(online)
            .scaleX(1f, 0.9f)
            .duration(1000)
            .repeatMode(ViewAnimator.REVERSE)
            .repeatCount(ValueAnimator.INFINITE)
            .start()

        // 心跳
        val intent = Intent(this, BeatService::class.java)
        startService(intent)
    }

    override fun initData() {
        super.initData()
        idCardOwner.setIdCardListener(this)
        idCardOwner.getReadIdCard()
    }

    // 初始化轮播图
    private fun initBanner() {
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR)
        banner.setImageLoader(GlideImageLoader())
        banner.setBannerAnimation(Transformer.DepthPage)
        banner.isAutoPlay(true)
        banner.setDelayTime(3000)
        banner.setIndicatorGravity(BannerConfig.RIGHT)
        banner.start()
        getCacheImg()
    }

    // 获取缓存中的轮播图
    private fun getCacheImg() {
        getCache(jsonImages, Consumer {
            if (!StringUtils.isEmpty(it)) {
                val images = JsonUtils.toAny(
                    it,
                    object : TypeToken<MutableList<String>>() {}) as MutableList<String>
                if (!images.isNullOrEmpty()) {
                    banner.update(images)
                }
                LogUtils.d("获取缓存轮播图")
            }
        })
    }

    override fun queryNoticeSuccess(notice: Notice) {
        this.notice = notice
        val ip = getIp()
        val images = mutableListOf<String>()
        notice.SplashImages?.forEach { images.add("$ip$it") }

        // 把图片连接缓存起来
        if (!images.isNullOrEmpty()) {
            putCache(jsonImages, JsonUtils.toJson(images))
        }

        banner.update(images)
    }

    // 身份证读取回调,在首页先调用一下,
    override fun idCardListener(cardInfo: CardInfo) {
    }

    override fun onResume() {
        super.onResume()
        // 如果IP端口为空就跳转设置页面
        if (StringUtils.isEmpty(getIp())) {
            val intent = Intent(this, GestureActivity::class.java)
            intent.putExtra("source", home)
            startAct(this, intent, isFinish = false)
        } else {
            mPresenter?.queryNotice()
        }
    }

    private var confirmPopupView: ConfirmPopupView? = null
    @BusUtils.Bus(tag = StartZZJ)
    fun startZZj(param: String) {
        confirmPopupView?.dismiss()
    }

    @BusUtils.Bus(tag = StopZZJ)
    fun stopZZJ(param: String) {
        if (confirmPopupView == null) {
            confirmPopupView = XPopup.Builder(this)
                .hasShadowBg(false)
                .dismissOnBackPressed(false).dismissOnTouchOutside(false)
                .asConfirm("", "$param", "", "", null, null, true)
        }
        confirmPopupView?.bindLayout(R.layout.dialog_zzj_stop)
        confirmPopupView?.show()
    }

    override fun onStart() {
        super.onStart()
        BusUtils.register(this)
        banner.startAutoPlay()
    }

    override fun onStop() {
        super.onStop()
        BusUtils.unregister(this)
        banner.stopAutoPlay()
    }

    override fun onPause() {
        super.onPause()
        idCardOwner.stopReadIdCard()
    }

    override fun onDestroy() {
        super.onDestroy()
        banner.releaseBanner()
    }

}