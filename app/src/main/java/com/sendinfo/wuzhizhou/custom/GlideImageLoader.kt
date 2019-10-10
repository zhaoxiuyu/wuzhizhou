package com.sendinfo.wuzhizhou.custom

import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.LogUtils
import com.bumptech.glide.Glide
import com.youth.banner.loader.ImageLoader

/**
 * 轮播图 加载器
 */
class GlideImageLoader : ImageLoader() {

    override fun createImageView(context: Context?): ImageView {
        return super.createImageView(context)
    }

    override fun displayImage(context: Context, path: Any, imageView: ImageView) {
        LogUtils.d(path.toString())
        Glide.with(context).load(path.toString()).into(imageView)
    }

}