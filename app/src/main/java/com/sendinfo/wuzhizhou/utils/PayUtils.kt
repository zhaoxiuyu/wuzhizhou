package com.sendinfo.wuzhizhou.utils

/**
 * Created by faliny on 2017/8/25.
 */

import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.TimeUtils
import org.apache.commons.codec.digest.DigestUtils

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*

object PayUtils {

    fun makeSign(md5Key: String, params: Map<String, String>): String {
        val preStr = buildSignString(params) // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        val text = preStr + md5Key
        return DigestUtils.md5Hex(getContentBytes(text)).toUpperCase()
    }

    fun checkSign(md5Key: String, params: Map<String, String>): Boolean {
        val sign = params["sign"]

        if (StringUtils.isEmpty(sign)) {
            return false
        }

        val signV = makeSign(md5Key, params)

        return StringUtils.equalsIgnoreCase(sign, signV)
    }

    fun genMerOrderId(msgId: String): String {
        return msgId + TimeUtils.date2String(Date(), "yyyyMMddmmHHssSSS") + ((Math.random() * 9 + 1) * 1000000).toInt()
    }

    fun buildUrlParametersStr(paramMap: Map<String, String>): String {
        val buffer = StringBuffer()
        val iterator = paramMap.entries.iterator()
        while (iterator.hasNext()) {
            var entry = iterator.next()
            buffer.append(entry.key).append("=")
            try {
                if (entry.value != null && !StringUtils.isEmpty(entry.value)) {
                    buffer.append(URLEncoder.encode(entry.value, "UTF-8"))
                }
            } catch (e: UnsupportedEncodingException) {
            }

            buffer.append(if (iterator.hasNext()) "&" else "")
        }
        return buffer.toString()
    }
    // 构建签名字符串
    fun buildSignString(params: Map<String, String>?): String {
        if (params == null || params.isEmpty()) {
            return ""
        }
        val keys = ArrayList<String>(params.size)
        for (key in params.keys) {
            if ("sign" == key)
                continue
            if (StringUtils.isEmpty(params[key]))
                continue
            keys.add(key)
        }
        keys.sort()
        val buf = StringBuilder()
        for (i in keys.indices) {
            val key = keys[i]
            val value = params[key]
            if (i == keys.size - 1) {// 拼接时，不包括最后一个&字符
                buf.append("$key=$value")
            } else {
                buf.append("$key=$value&")
            }
        }
        return buf.toString()
    }

    // 根据编码类型获得签名内容byte[]
    fun getContentBytes(content: String): ByteArray {
        try {
            return content.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("签名过程中出现错误")
        }
    }

}
