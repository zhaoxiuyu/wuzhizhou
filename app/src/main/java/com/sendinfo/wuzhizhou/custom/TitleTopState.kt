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
        updatePrintNumber()
        back.setOnClickListener { ActivityUtils.getActivityByContext(context).finish() }
    }

    fun updatePrintNumber() {
        printNumber.text = "可售数量 : ${getPrintNumber()}"
    }

    /**
     * 返回按钮点击事件
     */
    fun setBackOnClick(click: OnClickListener) {
        back.setOnClickListener(click)
    }

    /**
     * 设置返回按钮的显示隐藏
     */
    fun setBackVisibility(visibility: Int) {
        back.visibility = visibility
    }

    /**
     * countTime 总倒计时时间,单位:毫秒
     * surplusListener 倒计时结束回调接口,为空直接finish页面,不为空则回调该接口
     */
    private var surplus: OnSurplusListener? = null
    var myCountDownTimer: CountDownTimer? = null

    fun setOnSurplusListener(surplus: OnSurplusListener? = null) {
        this.surplus = surplus
    }

    fun startSurplus(countTime: Int = 30000) {
        djTime.visibility = View.VISIBLE
        djTime.text = "倒计时 ${(countTime / 1000)}"

        myCountDownTimer?.cancel() // 每次启动之前先取消倒计时

        // 新创建一个倒计时
        myCountDownTimer = MyCountDownTimer(countTime.toLong(), 1000, object : MyCountDownTimer.MyFinishTick {
            override fun onTick(millisUntilFinished: Long) {
                djTime.text = "倒计时 ${(millisUntilFinished / 1000)} s"
            }

            override fun onFinish() {
                djTime.text = "倒计时 0 s"
                if (surplus == null) {
                    ActivityUtils.getActivityByContext(context)?.finish()
                } else {
                    surplus?.surplus()
                }
            }
        })
        myCountDownTimer?.start() // 开始启动倒计时
    }

    /**
     * 连续点击三次
     */
    private val mHits = LongArray(3)

    fun setDouble(click: OnClickListener) {
        ivCurrent.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()//获取手机开机时间
            if (mHits[mHits.size - 1] - mHits[0] < 500) click.onClick(ivCurrent)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        myCountDownTimer?.cancel()
        surplus = null
    }

}