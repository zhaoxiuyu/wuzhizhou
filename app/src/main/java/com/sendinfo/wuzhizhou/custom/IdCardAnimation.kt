package com.sendinfo.wuzhizhou.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import com.github.florent37.viewanimator.ViewAnimator
import com.sendinfo.wuzhizhou.R
import kotlinx.android.synthetic.main.animation_id_card.view.*

/**
 * 身份证动画
 */
class IdCardAnimation(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {

    private var animator: ViewAnimator? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.animation_id_card, this)
    }

    fun startAnimation() {
        hengxian.visibility = View.VISIBLE

        animator = ViewAnimator.animate(hengxian)
            .translationY(0f, ivBg.height.toFloat())
            .duration(1500)
            .repeatCount(-1)
            .repeatMode(ViewAnimator.RESTART)
            .accelerate()
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
    }

}