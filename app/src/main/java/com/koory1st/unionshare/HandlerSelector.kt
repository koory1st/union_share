package com.koory1st.unionshare

import com.koory1st.unionshare.handler.JdHandler
import com.koory1st.unionshare.handler.TbHandler

class HandlerSelector {
    companion object {
        fun select(url: String): Result<String> {
            if (url.contains("京东")) {
                return JdHandler(url).request()
            }
            if (url.contains("淘宝")) {
                return TbHandler(url).request()
            }

            return Result.failure(Exception("None match"))
        }
    }
}