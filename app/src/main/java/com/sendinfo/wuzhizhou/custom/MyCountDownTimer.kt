package com.sendinfo.wuzhizhou.custom

import android.os.CountDownTimer

class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long, finishTick: MyFinishTick) :
    CountDownTimer(millisInFuture, countDownInterval) {

    var finishTick: MyFinishTick = finishTick

    override fun onFinish() {
        finishTick.onFinish()
    }

    override fun onTick(millisUntilFinished: Long) {
        finishTick.onTick(millisUntilFinished)
    }

    interface MyFinishTick {
        fun onFinish()

        fun onTick(millisUntilFinished: Long)
    }

}

