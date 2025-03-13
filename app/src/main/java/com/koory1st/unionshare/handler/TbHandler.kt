package com.koory1st.unionshare.handler

import android.util.Log
import com.koory1st.unionshare.BuildConfig
import com.taobao.api.internal.util.StringUtils
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Arrays
import java.util.Locale


class TbHandler(private val url: String) : RequestUnion {
    override fun request(): Result<String> {
        val final_url = getFinalUrl(url)
        val connection =
            URL(final_url).openConnection() as HttpURLConnection
        connection.setRequestMethod("GET")
        connection.connect()
        val responseCode = connection.getResponseCode()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            //得到响应流
            val inputStream = connection.inputStream
            val result: String = inputStream.bufferedReader().use { it.readText() }
            Log.d("kwwl", "result=============$result")
        }

//        val okHttpClient = OkHttpClient()
//        val request: Request = Request.Builder()
//            .url(final_url)
//            .get()
//            .build()
//        val call: Call = okHttpClient.newCall(request)
//        val response: Response = call.execute()
//        val body = response.body()
//        Log.d("aaa", body.toString())

        return Result.success("")
    }

    private fun getFinalUrl(url: String): String {
        var ret = ""
        val params = HashMap<String, String>()
        params["method"] = "taobao.tbk.item.convert"
        params["app_key"] = BuildConfig.TB_APP_KEY
        params["timestamp"] =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())
//        params["timestamp"] = URLEncoder.encode("2024-04-14 13:38:00")
        params["format"] = "json"
        params["v"] = "2.0"
        params["partner_id"] = "top-apitools"
        params["sign_method"] = "md5"
        params["requests"] = """[
  {
    "url": "$url"
  }
]"""
//        params["requests"] = URLEncoder.encode(
//            """[
//  {
//    "url": "【淘宝】https://m.tb.cn/h.5AMOUBE?tk=o63GWJTAyv1 CZ0002 「[88%棉]little moco童装春秋儿童裤子男女童宽松卫裤束脚休闲裤」
//点击链接直接打开 或者 淘宝搜索直接打开"
//  }
//]"""
        params["sign"] = signTopRequest(params, BuildConfig.TB_APP_SECRET)

        for (param in params.keys) {
            if (ret.isNotEmpty()) {
                ret += "&"
            }
            ret += "$param=${URLEncoder.encode(params[param])}"
        }

        return "${BuildConfig.TB_URL}?$ret"
    }

    private fun byte2hex(bytes: ByteArray): String {
        val sign = java.lang.StringBuilder()
        for (i in bytes.indices) {
            val hex = Integer.toHexString(bytes[i].toInt() and 0xFF)
            if (hex.length == 1) {
                sign.append("0")
            }
            sign.append(hex.uppercase(Locale.getDefault()))
        }
        return sign.toString()
    }

    @Throws(IOException::class)
    fun signTopRequest(params: Map<String, String?>, secret: String): String {
        // 第一步：检查参数是否已经排序
        val keys = params.keys.toTypedArray<String>()
        Arrays.sort(keys)

        // 第二步：把所有参数名和参数值串在一起
        val query = StringBuilder()
        query.append(secret)
        for (key in keys) {
            val value = params[key]
            if (StringUtils.areNotEmpty(key, value)) {
                query.append(key).append(value)
            }
        }

        // 第三步：使用MD5/HMAC加密
        query.append(secret)
        val bytes = encryptMD5(query.toString());

        // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
        return byte2hex(bytes)
    }

    @Throws(IOException::class)
    fun encryptMD5(data: String): ByteArray {
        return encryptMD5(data.toByteArray())
    }

    @Throws(IOException::class)
    fun encryptMD5(data: ByteArray): ByteArray {
        val bytes = try {
            val md = MessageDigest.getInstance("MD5")
            md.digest(data)
        } catch (gse: GeneralSecurityException) {
            throw IOException(gse.toString())
        }
        return bytes
    }
}