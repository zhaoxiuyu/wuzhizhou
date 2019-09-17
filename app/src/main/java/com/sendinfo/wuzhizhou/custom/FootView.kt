package com.sendinfo.wuzhizhou.custom

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.blankj.utilcode.util.AppUtils
import com.sendinfo.wuzhizhou.R
import com.sendinfo.wuzhizhou.utils.getShebeiCode
import kotlinx.android.synthetic.main.footview.view.*

/**
 * 底部版权
 */
class FootView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.footview, this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        tvFoot.text = "深大智能 • 智游宝 ©版权所有 本机编号 : ${getShebeiCode()} 系统版本号 : ${AppUtils.getAppVersionName()}"
    }

    /**
     * 连续点击三次
     */
    private val mHits = LongArray(3)

    fun setDouble(click: OnClickListener) {
        tvFoot.visibility = View.VISIBLE
        tvFoot.setOnClickListener {
            System.arraycopy(mHits, 1, mHits, 0, mHits.size - 1)
            mHits[mHits.size - 1] = SystemClock.uptimeMillis()//获取手机开机时间
            if (mHits[mHits.size - 1] - mHits[0] < 500) click.onClick(tvFoot)
        }
    }

}