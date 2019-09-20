package com.sendinfo.wuzhizhou.custom

import android.content.Context
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.base.library.interfaces.OnSurplusListener
import com.blankj.utilcode.util.ActivityUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.utils.getPrintNumber
import kotlinx.android.synthetic.main.title_top_state.view.*

/**
 * 页面顶部状态
 */
class TitleTopState(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.title_top_state, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        startSurplus()
        setPrintNumber()
    }

    fun setPrintNumber() {
        printNumber.text = "可售数量 : ${getPrintNumber()}"
    }

    /**
     * countTime 总倒计时时间,单位:毫秒
     * surplusListener 倒计时结束回调接口
     * finish 倒计时结束是否自动销毁页面
     */
    private var surplus: OnSurplusListener? = null
    private var countTime: Int = 30000
    private var finish: Boolean = true

    private fun startSurplus(countTime: Int = 30000, surplus: OnSurplusListener? = null, finish: Boolean = true) {
        this.surplus = surplus
        this.countTime = countTime
        this.finish = finish

        djTime.visibility = View.VISIBLE
        djTime.text = "倒计时 ${(countTime / 1000)}"
        cdTimer?.start()
    }

    fun stopSurplus() {
        cdTimer?.cancel()
    }

    private val cdTimer = object : CountDownTimer(countTime.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            djTime.text = "倒计时 ${(millisUntilFinished / 1000)} s"
        }

        override fun onFinish() {
            djTime.text = "倒计时 0 s"
            if (finish) ActivityUtils.getActivityByContext(getContext())?.finish()
            surplus?.surplus()
        }
    }

    /**
     * 连续点击三次
     */
    private val mHits = LongArray(3)

    fun setDouble(click: OnClickListener) {
        gongzhonghao.visibility = View.VISIBLE
        gongzhonghao.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()//获取手机开机时间
            if (mHits[mHits.size - 1] - mHits[0] < 500) click.onClick(gongzhonghao)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cdTimer?.cancel()
        surplus = null
    }

}