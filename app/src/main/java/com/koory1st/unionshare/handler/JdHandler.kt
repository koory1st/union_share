package com.koory1st.unionshare.handler

import android.util.Log
import com.jd.open.api.sdk.DefaultJdClient
import com.jd.open.api.sdk.domain.kplunion.promotioncommon.PromotionService.request.get.PromotionCodeReq
import com.jd.open.api.sdk.request.kplunion.UnionOpenPromotionCommonGetRequest
import com.koory1st.unionshare.BuildConfig

class JdHandler(private val url: String) : RequestUnion {

    override fun request(): Result<String> {
        val jdClient = DefaultJdClient(
            BuildConfig.JD_URL,
            "",
            BuildConfig.JD_APP_KEY,
            BuildConfig.JD_APP_SECRET
        )
        // jd.union.open.promotion.bysubunionid.get

        val regex = Regex("https://[\\w\\.\\/\\-]*")

        val newUrl = regex.find(url)?.value

        val request = UnionOpenPromotionCommonGetRequest()
        request.promotionCodeReq = PromotionCodeReq().apply {
            materialId = newUrl
            siteId = "4101240411"
            sceneId = 2
        }

        val resp = jdClient.execute(request)
        val openAppUrl = resp.getResult.run {
            if (code != 200) {
                Log.e("MainActivity", message)
                return Result.failure(Exception(message))
            }
            val clickURL = data.clickURL
            Log.d("MainActivity", clickURL)

            val openAppUrl =
                """openApp.jdMobile://virtual?params={"category":"jump","des":"m","url":"$clickURL","keplerID":"0","keplerForm":"1","kepler_param":{"source":"kepler-open","channel":""}}"""

            openAppUrl
        }
        return Result.success(openAppUrl)
    }

}
