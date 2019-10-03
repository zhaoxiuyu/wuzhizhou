package com.sendinfo.wuzhizhou.custom

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.sendinfo.wuzhizhou.R

class InfoDialog(context: Context) : Dialog(context, R.style.alert_dialog) {
    private var mTitleTextView: TextView? = null
    private var tvInfo: TextView? = null
    private var mConfirmButton: Button? = null
    private var mConfirmClickListener: View.OnClickListener? = null
    private var mTitleText: String? = null
    private var mConfirmText: String? = null
    private var infoText: String? = null

    init {
        window?.setGravity(Gravity.CENTER)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.info_dialog)
        mTitleTextView = findViewById(R.id.title_text)
        tvInfo = findViewById(R.id.tvInfo)
        tvInfo?.movementMethod = ScrollingMovementMethod.getInstance()
        mConfirmButton = findViewById(R.id.confirm_button)
        mConfirmButton?.setOnClickListener {
            if (mConfirmClickListener != null) {
                mConfirmClickListener?.onClick(it)
            } else {
                dismiss()
            }
        }
        findViewById<ImageView>(R.id.ivClose).setOnClickListener { dismiss() }
        if (mTitleText != null) {
            mTitleTextView?.text = mTitleText
        }
        if (infoText != null) {
            tvInfo?.text = infoText
        }
        if (mConfirmText != null) {
            mConfirmButton?.text = mConfirmText
        }
    }

    fun setContentText(infoText: String): InfoDialog {
        this.infoText = infoText
        return this
    }

    fun setTitleText(mTitleText: String): InfoDialog {
        this.mTitleText = mTitleText
        return this
    }

    fun setConfirmText(text: String): InfoDialog {
        mConfirmText = text
        return this
    }

    fun setConfirmClickListener(listener: View.OnClickListener): InfoDialog {
        mConfirmClickListener = listener
        return this
    }
}